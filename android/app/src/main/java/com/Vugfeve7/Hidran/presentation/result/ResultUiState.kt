package com.Vugfeve7.Hidran.presentation.result

import com.Vugfeve7.Hidran.domain.model.RiskMode
import com.Vugfeve7.Hidran.domain.model.SlotSymbol

data class ResultUiState(
    val ready: Boolean = false,
    val roomIndex: Int = 0,
    val mode: RiskMode = RiskMode.BOLD,
    val staked: Int = 0,
    val won: Int = 0,
    val stars: Int = 0,
    val bestPayout: Int = 0,
    val streakPeakPercent: Int = 100,
    val newCards: List<SlotSymbol> = emptyList(),
    val setBonus: Int = 0,
    val bust: Boolean = false,
    val refillClaimed: Boolean = false,
    val coins: Int = 0
) {
    val net: Int
        get() = won - staked

    val canSpinAgain: Boolean
        get() = !bust || refillClaimed
}
