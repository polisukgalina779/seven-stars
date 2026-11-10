package com.Vugfeve7.Hidran.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vugfeve7.Hidran.data.sample.SampleData
import com.Vugfeve7.Hidran.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUiState())
    val state: StateFlow<ProgressUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.profile.collect { profile ->
                _state.value = ProgressUiState(
                    coins = profile.coins,
                    stars = profile.starTotal,
                    totalSpins = profile.totalSpins,
                    bestWin = profile.bestWin,
                    roomsUnlocked = SampleData.rooms.count { profile.isUnlocked(it.index) },
                    cardsFound = profile.cardTotal,
                    mastery = SampleData.modes.map { mode ->
                        ModeMastery(
                            mode = mode,
                            spins = profile.modeSpins[mode] ?: 0,
                            bestRun = profile.modeBestRun[mode] ?: 0
                        )
                    },
                    ladder = SampleData.rooms.map { room ->
                        RoomLadderEntry(
                            roomIndex = room.index,
                            stars = profile.starsIn(room.index),
                            goal = room.goalThreeStars,
                            unlocked = profile.isUnlocked(room.index)
                        )
                    }
                )
            }
        }
    }
}
