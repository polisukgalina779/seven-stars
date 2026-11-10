package com.Vugfeve7.Hidran.domain.usecase

import com.Vugfeve7.Hidran.domain.model.Room
import com.Vugfeve7.Hidran.domain.repository.ProgressRepository

class UnlockRoomUseCase(
    private val progressRepository: ProgressRepository
) {

    operator fun invoke(room: Room): Boolean {
        val current = progressRepository.profile.value
        if (current.isUnlocked(room.index)) return true
        if (current.coins < room.unlockPrice) return false
        progressRepository.update { profile ->
            profile.copy(
                coins = profile.coins - room.unlockPrice,
                unlockedRooms = profile.unlockedRooms + room.index
            )
        }
        return true
    }
}
