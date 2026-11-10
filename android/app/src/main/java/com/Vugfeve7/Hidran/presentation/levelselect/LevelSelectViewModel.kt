package com.Vugfeve7.Hidran.presentation.levelselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vugfeve7.Hidran.core.config.GameConfig
import com.Vugfeve7.Hidran.data.sample.SampleData
import com.Vugfeve7.Hidran.domain.model.RiskMode
import com.Vugfeve7.Hidran.domain.repository.ProgressRepository
import com.Vugfeve7.Hidran.domain.repository.SettingsRepository
import com.Vugfeve7.Hidran.domain.usecase.UnlockRoomUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LevelSelectViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val unlockRoom: UnlockRoomUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LevelSelectUiState())
    val state: StateFlow<LevelSelectUiState> = _state.asStateFlow()

    private var warningJob: Job? = null

    init {
        viewModelScope.launch {
            combine(progressRepository.profile, settingsRepository.settings) { profile, settings ->
                val rooms = SampleData.rooms.map { room ->
                    RoomCardState(
                        index = room.index,
                        unlocked = profile.isUnlocked(room.index),
                        stars = profile.starsIn(room.index),
                        goal = room.goalThreeStars,
                        price = room.unlockPrice,
                        affordable = profile.coins >= room.unlockPrice,
                        stake = room.stakeFor(settings.defaultMode)
                    )
                }
                Triple(profile.coins, settings.defaultMode, rooms)
            }.collect { snapshot ->
                _state.update { current ->
                    current.copy(
                        coins = snapshot.first,
                        mode = snapshot.second,
                        rooms = snapshot.third
                    )
                }
            }
        }
    }

    fun selectMode(mode: RiskMode) {
        settingsRepository.setDefaultMode(mode)
    }

    fun unlock(roomIndex: Int) {
        val room = SampleData.room(roomIndex)
        if (unlockRoom(room)) {
            clearWarning()
        } else {
            flashWarning()
        }
    }

    fun enter(roomIndex: Int) {
        progressRepository.update { profile -> profile.copy(activeRoom = roomIndex) }
    }

    fun spinsPerRun(): Int = GameConfig.SPINS_PER_RUN

    private fun clearWarning() {
        warningJob?.cancel()
        _state.update { it.copy(warning = false) }
    }

    private fun flashWarning() {
        warningJob?.cancel()
        warningJob = viewModelScope.launch {
            _state.update { it.copy(warning = true) }
            delay(2000L)
            _state.update { it.copy(warning = false) }
        }
    }
}
