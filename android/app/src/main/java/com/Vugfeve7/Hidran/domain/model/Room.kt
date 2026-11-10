package com.Vugfeve7.Hidran.domain.model

data class Room(
    val index: Int,
    val unlockPrice: Int,
    val goalThreeStars: Int,
    val goalTwoStars: Int,
    val goalOneStar: Int,
    val stakeMultiplierPercent: Int
) {
    fun stakeFor(mode: RiskMode): Int = mode.stake * stakeMultiplierPercent / 100
}
