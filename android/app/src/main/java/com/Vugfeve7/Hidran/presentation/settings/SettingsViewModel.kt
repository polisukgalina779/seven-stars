package com.Vugfeve7.Hidran.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vugfeve7.Hidran.domain.model.ReelSpeed
import com.Vugfeve7.Hidran.domain.model.RiskMode
import com.Vugfeve7.Hidran.domain.repository.ProgressRepository
import com.Vugfeve7.Hidran.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _state.update { current ->
                    current.copy(
                        defaultMode = settings.defaultMode,
                        reelSpeed = settings.reelSpeed,
                        reduceMotion = settings.reduceMotion,
                        showPayTable = settings.showPayTable
                    )
                }
            }
        }
    }

    fun selectMode(mode: RiskMode) {
        settingsRepository.setDefaultMode(mode)
    }

    fun selectSpeed(speed: ReelSpeed) {
        settingsRepository.setReelSpeed(speed)
    }

    fun toggleReduceMotion(enabled: Boolean) {
        settingsRepository.setReduceMotion(enabled)
    }

    fun togglePayTable(enabled: Boolean) {
        settingsRepository.setShowPayTable(enabled)
    }

    fun askReset() {
        _state.update { it.copy(confirmReset = true) }
    }

    fun cancelReset() {
        _state.update { it.copy(confirmReset = false) }
    }

    fun confirmReset() {
        progressRepository.reset()
        settingsRepository.reset()
        _state.update { it.copy(confirmReset = false) }
    }
}
