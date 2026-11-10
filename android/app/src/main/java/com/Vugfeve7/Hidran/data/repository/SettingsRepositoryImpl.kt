package com.Vugfeve7.Hidran.data.repository

import com.Vugfeve7.Hidran.data.local.PreferencesStore
import com.Vugfeve7.Hidran.domain.model.AppSettings
import com.Vugfeve7.Hidran.domain.model.ReelSpeed
import com.Vugfeve7.Hidran.domain.model.RiskMode
import com.Vugfeve7.Hidran.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val store: PreferencesStore
) : SettingsRepository {

    private val _settings = MutableStateFlow(load())
    override val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    override fun setDefaultMode(mode: RiskMode) {
        store.writeText(KEY_MODE, mode.name)
        _settings.value = _settings.value.copy(defaultMode = mode)
    }

    override fun setReelSpeed(speed: ReelSpeed) {
        store.writeText(KEY_SPEED, speed.name)
        _settings.value = _settings.value.copy(reelSpeed = speed)
    }

    override fun setReduceMotion(enabled: Boolean) {
        store.writeBoolean(KEY_REDUCE_MOTION, enabled)
        _settings.value = _settings.value.copy(reduceMotion = enabled)
    }

    override fun setShowPayTable(enabled: Boolean) {
        store.writeBoolean(KEY_PAY_TABLE, enabled)
        _settings.value = _settings.value.copy(showPayTable = enabled)
    }

    override fun reset() {
        store.writeText(KEY_MODE, RiskMode.BOLD.name)
        store.writeText(KEY_SPEED, ReelSpeed.STANDARD.name)
        store.writeBoolean(KEY_REDUCE_MOTION, false)
        store.writeBoolean(KEY_PAY_TABLE, true)
        _settings.value = AppSettings()
    }

    private fun load(): AppSettings {
        val modeName = store.readText(KEY_MODE, RiskMode.BOLD.name)
        val speedName = store.readText(KEY_SPEED, ReelSpeed.STANDARD.name)
        return AppSettings(
            defaultMode = RiskMode.entries.firstOrNull { it.name == modeName } ?: RiskMode.BOLD,
            reelSpeed = ReelSpeed.entries.firstOrNull { it.name == speedName } ?: ReelSpeed.STANDARD,
            reduceMotion = store.readBoolean(KEY_REDUCE_MOTION, false),
            showPayTable = store.readBoolean(KEY_PAY_TABLE, true)
        )
    }

    companion object {
        private const val KEY_MODE = "default_mode"
        private const val KEY_SPEED = "reel_speed"
        private const val KEY_REDUCE_MOTION = "reduce_motion"
        private const val KEY_PAY_TABLE = "show_pay_table"
    }
}
