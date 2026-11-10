package com.Vugfeve7.Hidran.presentation.progress

import com.Vugfeve7.Hidran.domain.model.RiskMode

data class ModeMastery(
    val mode: RiskMode,
    val spins: Int,
    val bestRun: Int
)

data class RoomLadderEntry(
    val roomIndex: Int,
    val stars: Int,
    val goal: Int,
    val unlocked: Boolean
)

data class ProgressUiState(
    val coins: Int = 0,
    val stars: Int = 0,
    val totalSpins: Int = 0,
    val bestWin: Int = 0,
    val roomsUnlocked: Int = 1,
    val cardsFound: Int = 0,
    val mastery: List<ModeMastery> = emptyList(),
    val ladder: List<RoomLadderEntry> = emptyList()
) {
    val empty: Boolean
        get() = totalSpins == 0
}
