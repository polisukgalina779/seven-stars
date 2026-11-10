package com.Vugfeve7.Hidran.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vugfeve7.Hidran.data.sample.SampleData
import com.Vugfeve7.Hidran.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.profile.collect { profile ->
                val rooms = SampleData.rooms.map { room ->
                    RoomCardSet(
                        roomIndex = room.index,
                        owned = SampleData.symbols.map { symbol -> profile.hasCard(room.index, symbol) },
                        symbols = SampleData.symbols
                    )
                }
                _state.value = CollectionUiState(
                    coins = profile.coins,
                    earnedTotal = profile.cardTotal,
                    rooms = rooms
                )
            }
        }
    }
}
