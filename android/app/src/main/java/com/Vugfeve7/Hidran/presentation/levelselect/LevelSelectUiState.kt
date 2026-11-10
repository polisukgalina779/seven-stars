package com.Vugfeve7.Hidran.presentation.levelselect

import com.Vugfeve7.Hidran.domain.model.RiskMode

data class RoomCardState(
    val index: Int,
    val unlocked: Boolean,
    val stars: Int,
    val goal: Int,
    val price: Int,
    val affordable: Boolean,
    val stake: Int
)

data class LevelSelectUiState(
    val coins: Int = 0,
    val mode: RiskMode = RiskMode.BOLD,
    val rooms: List<RoomCardState> = emptyList(),
    val warning: Boolean = false
)
