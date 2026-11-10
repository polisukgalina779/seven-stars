package com.Vugfeve7.Hidran.domain.model

data class PayLineWin(
    val lineIndex: Int,
    val symbol: SlotSymbol,
    val amount: Int
)

data class SpinOutcome(
    val strips: List<List<SlotSymbol>>,
    val grid: List<List<SlotSymbol>>,
    val wins: List<PayLineWin>,
    val baseWin: Int,
    val bestPayout: Int,
    val seed: Long
)
