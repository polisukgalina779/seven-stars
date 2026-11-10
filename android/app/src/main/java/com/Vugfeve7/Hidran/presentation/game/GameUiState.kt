package com.Vugfeve7.Hidran.presentation.game

import com.Vugfeve7.Hidran.core.config.GameConfig
import com.Vugfeve7.Hidran.domain.model.RiskMode
import com.Vugfeve7.Hidran.domain.model.SlotSymbol

enum class GameStatus {
    Idle,
    Spinning,
    Evaluating,
    Winning,
    RunComplete,
    Bust
}

data class GameUiState(
    val roomIndex: Int = 0,
    val mode: RiskMode = RiskMode.BOLD,
    val stake: Int = RiskMode.BOLD.stake,
    val goal: Int = 120,
    val coins: Int = GameConfig.START_COINS,
    val spinsDone: Int = 0,
    val status: GameStatus = GameStatus.Idle,
    val strips: List<List<SlotSymbol>> = emptyList(),
    val winningCells: Set<Int> = emptySet(),
    val winningLines: Set<Int> = emptySet(),
    val lastWin: Int = 0,
    val runWon: Int = 0,
    val streakPercent: Int = 100,
    val streakStep: Int = 0,
    val wildBoost: Boolean = false,
    val bigWin: Boolean = false,
    val spinToken: Int = 0,
    val reelDurations: List<Int> = listOf(900, 1250, 1600),
    val paused: Boolean = false,
    val showPayTable: Boolean = true,
    val reduceMotion: Boolean = false
) {
    val runNumber: Int
        get() = (spinsDone + 1).coerceAtMost(GameConfig.SPINS_PER_RUN)

    val canSpin: Boolean
        get() = status == GameStatus.Idle && !paused && coins >= stake

    val notEnoughCoins: Boolean
        get() = coins < stake
}
