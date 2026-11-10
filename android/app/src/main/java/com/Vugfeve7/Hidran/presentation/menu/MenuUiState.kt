package com.Vugfeve7.Hidran.presentation.menu

import com.Vugfeve7.Hidran.core.config.GameConfig
import com.Vugfeve7.Hidran.domain.model.RiskMode

data class MenuUiState(
    val coins: Int = GameConfig.START_COINS,
    val activeRoom: Int = 0,
    val mode: RiskMode = RiskMode.BOLD,
    val stake: Int = RiskMode.BOLD.stake,
    val roomsUnlocked: Int = 1,
    val cards: Int = 0,
    val stars: Int = 0,
    val bestWin: Int = 0,
    val totalSpins: Int = 0,
    val reduceMotion: Boolean = false
)
