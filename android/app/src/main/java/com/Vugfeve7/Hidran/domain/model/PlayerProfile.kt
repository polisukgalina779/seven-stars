package com.Vugfeve7.Hidran.domain.model

data class PlayerProfile(
    val coins: Int,
    val totalSpins: Int,
    val bestWin: Int,
    val unlockedRooms: Set<Int>,
    val roomStars: Map<Int, Int>,
    val cards: Set<String>,
    val paidSetBonuses: Set<Int>,
    val modeSpins: Map<RiskMode, Int>,
    val modeBestRun: Map<RiskMode, Int>,
    val activeRoom: Int
) {
    val starTotal: Int
        get() = roomStars.values.sum()

    val cardTotal: Int
        get() = cards.size

    fun starsIn(roomIndex: Int): Int = roomStars[roomIndex] ?: 0

    fun isUnlocked(roomIndex: Int): Boolean = roomIndex == 0 || unlockedRooms.contains(roomIndex)

    fun hasCard(roomIndex: Int, symbol: SlotSymbol): Boolean = cards.contains(cardKey(roomIndex, symbol))

    fun cardsIn(roomIndex: Int): Int = cards.count { it.startsWith("$roomIndex:") }

    companion object {
        fun cardKey(roomIndex: Int, symbol: SlotSymbol): String = "$roomIndex:${symbol.name}"
    }
}
