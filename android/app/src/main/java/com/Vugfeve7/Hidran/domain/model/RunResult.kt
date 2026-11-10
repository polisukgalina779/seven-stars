package com.Vugfeve7.Hidran.domain.model

data class CollectedCard(
    val roomIndex: Int,
    val symbol: SlotSymbol
)

data class RunResult(
    val roomIndex: Int,
    val mode: RiskMode,
    val staked: Int,
    val won: Int,
    val stars: Int,
    val bestPayout: Int,
    val streakPeakPercent: Int,
    val newCards: List<CollectedCard>,
    val setBonus: Int,
    val bust: Boolean
) {
    val net: Int
        get() = won - staked
}
