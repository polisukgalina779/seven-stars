package com.Vugfeve7.Hidran.domain.model

enum class RiskMode(
    val stake: Int,
    val weights: List<Int>,
    val payoutScalarPercent: Int
) {
    CALM(15, listOf(36, 26, 13, 4, 3), 105),
    BOLD(40, listOf(30, 22, 14, 7, 5), 100),
    WILD(100, listOf(22, 16, 12, 9, 4), 95)
}
