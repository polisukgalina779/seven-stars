package com.Vugfeve7.Hidran.domain.repository

import com.Vugfeve7.Hidran.domain.model.AppSettings
import com.Vugfeve7.Hidran.domain.model.ReelSpeed
import com.Vugfeve7.Hidran.domain.model.RiskMode
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<AppSettings>
    fun setDefaultMode(mode: RiskMode)
    fun setReelSpeed(speed: ReelSpeed)
    fun setReduceMotion(enabled: Boolean)
    fun setShowPayTable(enabled: Boolean)
    fun reset()
}
