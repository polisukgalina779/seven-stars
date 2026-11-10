package com.Vugfeve7.Hidran.data.sample

import com.Vugfeve7.Hidran.domain.model.ReelSpeed
import com.Vugfeve7.Hidran.domain.model.RiskMode
import com.Vugfeve7.Hidran.domain.model.Room
import com.Vugfeve7.Hidran.domain.model.SlotSymbol

object SampleData {

    val rooms: List<Room> = listOf(
        Room(index = 0, unlockPrice = 0, goalThreeStars = 120, goalTwoStars = 70, goalOneStar = 30, stakeMultiplierPercent = 100),
        Room(index = 1, unlockPrice = 600, goalThreeStars = 220, goalTwoStars = 130, goalOneStar = 55, stakeMultiplierPercent = 100),
        Room(index = 2, unlockPrice = 1500, goalThreeStars = 400, goalTwoStars = 240, goalOneStar = 100, stakeMultiplierPercent = 120),
        Room(index = 3, unlockPrice = 3000, goalThreeStars = 700, goalTwoStars = 420, goalOneStar = 175, stakeMultiplierPercent = 140),
        Room(index = 4, unlockPrice = 6000, goalThreeStars = 1150, goalTwoStars = 690, goalOneStar = 290, stakeMultiplierPercent = 160),
        Room(index = 5, unlockPrice = 12000, goalThreeStars = 1900, goalTwoStars = 1140, goalOneStar = 475, stakeMultiplierPercent = 200)
    )

    val symbols: List<SlotSymbol> = SlotSymbol.entries.toList()

    val modes: List<RiskMode> = RiskMode.entries.toList()

    val speeds: List<ReelSpeed> = ReelSpeed.entries.toList()

    fun room(index: Int): Room = rooms.getOrElse(index) { rooms.first() }

    fun nextUnlockPrice(roomIndex: Int): Int =
        rooms.getOrNull(roomIndex + 1)?.unlockPrice ?: rooms.last().unlockPrice
}
