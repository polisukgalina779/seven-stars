package com.Vugfeve7.Hidran.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vugfeve7.Hidran.data.sample.SampleData
import com.Vugfeve7.Hidran.domain.repository.ProgressRepository
import com.Vugfeve7.Hidran.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(progressRepository.profile, settingsRepository.settings) { profile, settings ->
                val activeRoom = if (profile.isUnlocked(profile.activeRoom)) profile.activeRoom else 0
                MenuUiState(
                    coins = profile.coins,
                    activeRoom = activeRoom,
                    mode = settings.defaultMode,
                    stake = SampleData.room(activeRoom).stakeFor(settings.defaultMode),
                    roomsUnlocked = SampleData.rooms.count { profile.isUnlocked(it.index) },
                    cards = profile.cardTotal,
                    stars = profile.starTotal,
                    bestWin = profile.bestWin,
                    totalSpins = profile.totalSpins,
                    reduceMotion = settings.reduceMotion
                )
            }.collect { snapshot ->
                _state.value = snapshot
            }
        }
    }

    fun rememberActiveRoom(roomIndex: Int) {
        progressRepository.update { profile -> profile.copy(activeRoom = roomIndex) }
    }
}
