package com.Vugfeve7.Hidran.presentation.settings

import com.Vugfeve7.Hidran.domain.model.ReelSpeed
import com.Vugfeve7.Hidran.domain.model.RiskMode

data class SettingsUiState(
    val defaultMode: RiskMode = RiskMode.BOLD,
    val reelSpeed: ReelSpeed = ReelSpeed.STANDARD,
    val reduceMotion: Boolean = false,
    val showPayTable: Boolean = true,
    val confirmReset: Boolean = false
)
