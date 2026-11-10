package com.Vugfeve7.Hidran.presentation.collection

import com.Vugfeve7.Hidran.domain.model.SlotSymbol

data class RoomCardSet(
    val roomIndex: Int,
    val owned: List<Boolean>,
    val symbols: List<SlotSymbol>
) {
    val earned: Int
        get() = owned.count { it }

    val complete: Boolean
        get() = owned.isNotEmpty() && owned.all { it }
}

data class CollectionUiState(
    val coins: Int = 0,
    val earnedTotal: Int = 0,
    val rooms: List<RoomCardSet> = emptyList()
)
