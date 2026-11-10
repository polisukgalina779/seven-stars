package com.Vugfeve7.Hidran.presentation.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vugfeve7.Hidran.core.config.GameConfig
import com.Vugfeve7.Hidran.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ResultUiState())
    val state: StateFlow<ResultUiState> = _state.asStateFlow()

    private var claimedForRun = false

    init {
        viewModelScope.launch {
            combine(progressRepository.lastRun, progressRepository.profile) { run, profile ->
                if (run == null) {
                    ResultUiState(ready = false, coins = profile.coins)
                } else {
                    ResultUiState(
                        ready = true,
                        roomIndex = run.roomIndex,
                        mode = run.mode,
                        staked = run.staked,
                        won = run.won,
                        stars = run.stars,
                        bestPayout = run.bestPayout,
                        streakPeakPercent = run.streakPeakPercent,
                        newCards = run.newCards.map { card -> card.symbol },
                        setBonus = run.setBonus,
                        bust = run.bust,
                        refillClaimed = claimedForRun,
                        coins = profile.coins
                    )
                }
            }.collect { snapshot ->
                _state.value = snapshot
            }
        }
    }

    fun claimRefill() {
        if (claimedForRun) return
        claimedForRun = true
        progressRepository.update { profile ->
            profile.copy(coins = profile.coins + GameConfig.REFILL_COINS)
        }
        _state.update { it.copy(refillClaimed = true) }
    }

    fun startNextRun() {
        claimedForRun = false
    }
}
