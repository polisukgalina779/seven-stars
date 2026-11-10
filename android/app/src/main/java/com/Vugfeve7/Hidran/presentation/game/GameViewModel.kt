package com.Vugfeve7.Hidran.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vugfeve7.Hidran.core.config.GameConfig
import com.Vugfeve7.Hidran.data.sample.SampleData
import com.Vugfeve7.Hidran.domain.model.CollectedCard
import com.Vugfeve7.Hidran.domain.model.PlayerProfile
import com.Vugfeve7.Hidran.domain.model.RiskMode
import com.Vugfeve7.Hidran.domain.model.Room
import com.Vugfeve7.Hidran.domain.model.RunResult
import com.Vugfeve7.Hidran.domain.model.SpinOutcome
import com.Vugfeve7.Hidran.domain.repository.ProgressRepository
import com.Vugfeve7.Hidran.domain.repository.SettingsRepository
import com.Vugfeve7.Hidran.domain.usecase.AwardStarsUseCase
import com.Vugfeve7.Hidran.domain.usecase.EvaluatePayLinesUseCase
import com.Vugfeve7.Hidran.domain.usecase.GrantCollectionCardUseCase
import com.Vugfeve7.Hidran.domain.usecase.SpinReelsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed interface GameEvent {
    data class RunComplete(val token: Long) : GameEvent
}

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val spinReels: SpinReelsUseCase,
    private val awardStars: AwardStarsUseCase,
    private val grantCard: GrantCollectionCardUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var spinJob: Job? = null
    private var backstopJob: Job? = null

    private var runIndex = 0
    private var runStaked = 0
    private var lossStreak = 0
    private var streakPeak = 100
    private var newCards = mutableListOf<CollectedCard>()
    private var setBonus = 0
    private var bestPayout = 0
    private var finished = false
    private var activeToken = 0L
    private var runStartedAt = 0L

    init {
        viewModelScope.launch {
            progressRepository.profile.collect { profile ->
                _state.update { current -> current.copy(coins = profile.coins) }
            }
        }
    }

    fun startRun(roomIndex: Int, mode: RiskMode, token: Long) {
        activeToken = token
        beginRun(roomIndex, mode, resetClock = true)
    }

    private fun beginRun(roomIndex: Int, mode: RiskMode, resetClock: Boolean) {
        spinJob?.cancel()
        backstopJob?.cancel()
        if (resetClock) runStartedAt = System.currentTimeMillis()
        runIndex += 1
        runStaked = 0
        lossStreak = 0
        streakPeak = 100
        newCards = mutableListOf()
        setBonus = 0
        bestPayout = 0
        finished = false

        val room = SampleData.room(roomIndex)
        val settings = settingsRepository.settings.value
        val profile = progressRepository.profile.value
        val stake = room.stakeFor(mode)
        val preview = spinReels(mode, stake / GameConfig.REELS, spinReels.seedFor(roomIndex, runIndex, 0), false)

        progressRepository.clearRun()
        _state.value = GameUiState(
            roomIndex = roomIndex,
            mode = mode,
            stake = stake,
            goal = room.goalThreeStars,
            coins = profile.coins,
            spinsDone = 0,
            status = if (profile.coins < stake) GameStatus.Bust else GameStatus.Idle,
            strips = preview.strips,
            reelDurations = durationsFor(settings.reelSpeed.multiplierPercent),
            showPayTable = settings.showPayTable,
            reduceMotion = settings.reduceMotion
        )

        if (profile.coins < stake) {
            spinJob = viewModelScope.launch {
                delay(1500L)
                finishRun(bust = true)
            }
        } else {
            armBackstop()
        }
    }

    fun spin() {
        val current = _state.value
        if (!current.canSpin) return
        backstopJob?.cancel()
        spinJob?.cancel()
        spinJob = viewModelScope.launch { runSpin(current) }
    }

    fun pause() {
        backstopJob?.cancel()
        _state.update { it.copy(paused = true) }
    }

    fun resume() {
        _state.update { it.copy(paused = false) }
        armBackstop()
    }

    fun restartRun() {
        val current = _state.value
        val refund = runStaked
        if (refund > 0) {
            progressRepository.update { profile -> profile.copy(coins = profile.coins + refund) }
        }
        beginRun(current.roomIndex, current.mode, resetClock = false)
    }

    override fun onCleared() {
        super.onCleared()
        spinJob?.cancel()
        backstopJob?.cancel()
    }

    private suspend fun runSpin(current: GameUiState) {
        val room = SampleData.room(current.roomIndex)
        val stake = current.stake
        val boost = lossStreak >= GameConfig.LOSS_STREAK_FOR_TILT
        val outcome = spinReels(
            current.mode,
            stake / GameConfig.REELS,
            spinReels.seedFor(current.roomIndex, runIndex, current.spinsDone + 1),
            boost
        )

        runStaked += stake
        progressRepository.update { profile ->
            profile.copy(
                coins = profile.coins - stake,
                totalSpins = profile.totalSpins + 1,
                modeSpins = profile.modeSpins + (current.mode to (profile.modeSpins[current.mode] ?: 0) + 1)
            )
        }

        _state.update {
            it.copy(
                status = GameStatus.Spinning,
                spinToken = it.spinToken + 1,
                strips = outcome.strips,
                winningCells = emptySet(),
                winningLines = emptySet(),
                lastWin = 0,
                bigWin = false,
                wildBoost = boost,
                coins = progressRepository.profile.value.coins
            )
        }

        delay(current.reelDurations.last().toLong())
        waitWhilePaused()

        _state.update { it.copy(status = GameStatus.Evaluating) }
        delay(GameConfig.EVALUATE_MS)
        waitWhilePaused()

        val multiplier = GameConfig.STREAK_PERCENT[_state.value.streakStep.coerceIn(0, 2)]
        val win = outcome.baseWin * multiplier / 100

        if (win > 0) {
            applyWin(room, outcome, win, multiplier)
            delay(GameConfig.WIN_CELEBRATION_MS)
            waitWhilePaused()
            lossStreak = 0
        } else {
            lossStreak += 1
            _state.update { it.copy(streakStep = 0, streakPercent = 100, lastWin = 0) }
        }

        val spins = _state.value.spinsDone + 1
        _state.update {
            it.copy(
                spinsDone = spins,
                status = GameStatus.Idle,
                winningCells = emptySet(),
                winningLines = emptySet(),
                bigWin = false,
                coins = progressRepository.profile.value.coins
            )
        }

        when {
            spins >= GameConfig.SPINS_PER_RUN -> {
                _state.update { it.copy(status = GameStatus.RunComplete) }
                delay(GameConfig.RUN_COMPLETE_MS)
                finishRun(bust = false)
            }

            _state.value.notEnoughCoins -> {
                _state.update { it.copy(status = GameStatus.Bust) }
                delay(1200L)
                finishRun(bust = true)
            }

            else -> armBackstop()
        }
    }

    private fun applyWin(room: Room, outcome: SpinOutcome, win: Int, multiplier: Int) {
        val cells = mutableSetOf<Int>()
        val lines = mutableSetOf<Int>()
        outcome.wins.forEach { lineWin ->
            lines.add(lineWin.lineIndex)
            val rows = EvaluatePayLinesUseCase.PAY_LINES[lineWin.lineIndex]
            rows.forEachIndexed { column, row -> cells.add(column * GameConfig.ROWS + row) }
        }

        val profile = progressRepository.profile.value
        val granted = grantCard(profile, room.index, outcome.wins.map { it.symbol })
        val completes = granted.isNotEmpty() && grantCard.completesSet(profile, room.index, granted)
        val bonus = if (completes) SampleData.nextUnlockPrice(room.index) * GameConfig.ROOM_SET_BONUS_PERCENT / 100 else 0

        newCards.addAll(granted)
        setBonus += bonus
        bestPayout = maxOf(bestPayout, outcome.bestPayout)

        progressRepository.update { current ->
            current.copy(
                coins = current.coins + win + bonus,
                bestWin = maxOf(current.bestWin, win),
                cards = current.cards + granted.map { PlayerProfile.cardKey(it.roomIndex, it.symbol) },
                paidSetBonuses = if (completes) current.paidSetBonuses + room.index else current.paidSetBonuses
            )
        }

        streakPeak = maxOf(streakPeak, multiplier)
        val nextStep = (_state.value.streakStep + 1).coerceAtMost(2)

        _state.update {
            it.copy(
                status = GameStatus.Winning,
                winningCells = cells,
                winningLines = lines,
                lastWin = win,
                runWon = it.runWon + win,
                bigWin = win >= it.stake * GameConfig.BIG_WIN_MULTIPLIER,
                streakStep = nextStep,
                streakPercent = GameConfig.STREAK_PERCENT[nextStep],
                coins = progressRepository.profile.value.coins
            )
        }
    }

    private fun finishRun(bust: Boolean) {
        if (finished) return
        finished = true
        backstopJob?.cancel()

        val current = _state.value
        val room = SampleData.room(current.roomIndex)
        val stars = awardStars(room, current.runWon)

        progressRepository.update { profile ->
            val best = maxOf(profile.starsIn(room.index), stars)
            profile.copy(
                roomStars = profile.roomStars + (room.index to best),
                modeBestRun = profile.modeBestRun + (current.mode to maxOf(profile.modeBestRun[current.mode] ?: 0, current.runWon)),
                activeRoom = room.index
            )
        }

        progressRepository.publishRun(
            RunResult(
                roomIndex = room.index,
                mode = current.mode,
                staked = runStaked,
                won = current.runWon,
                stars = stars,
                bestPayout = bestPayout,
                streakPeakPercent = streakPeak,
                newCards = newCards.toList(),
                setBonus = setBonus,
                bust = bust
            )
        )

        viewModelScope.launch { _events.send(GameEvent.RunComplete(activeToken)) }
    }

    private fun armBackstop() {
        backstopJob?.cancel()
        if (finished) return
        backstopJob = viewModelScope.launch {
            while (isActive) {
                val elapsed = System.currentTimeMillis() - runStartedAt
                delay(maxOf(AUTO_SPIN_STEP_MS, AUTO_SPIN_FLOOR_MS - elapsed))
                val current = _state.value
                if (current.paused) continue
                if (current.canSpin) {
                    spinJob?.cancel()
                    spinJob = viewModelScope.launch { runSpin(_state.value) }
                    return@launch
                }
                finishRun(bust = current.notEnoughCoins)
                return@launch
            }
        }
    }

    private suspend fun waitWhilePaused() {
        while (_state.value.paused) {
            delay(90L)
        }
    }

    private fun durationsFor(multiplierPercent: Int): List<Int> =
        GameConfig.REEL_STOP_MS.map { base -> (base * multiplierPercent / 100).toInt() }

    companion object {
        private const val AUTO_SPIN_FLOOR_MS = 30000L
        private const val AUTO_SPIN_STEP_MS = 3500L
    }
}
