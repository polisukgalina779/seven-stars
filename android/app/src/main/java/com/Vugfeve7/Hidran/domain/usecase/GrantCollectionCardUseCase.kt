package com.Vugfeve7.Hidran.domain.usecase

import com.Vugfeve7.Hidran.core.config.GameConfig
import com.Vugfeve7.Hidran.domain.model.CollectedCard
import com.Vugfeve7.Hidran.domain.model.PlayerProfile
import com.Vugfeve7.Hidran.domain.model.SlotSymbol

class GrantCollectionCardUseCase {

    operator fun invoke(
        profile: PlayerProfile,
        roomIndex: Int,
        symbols: List<SlotSymbol>
    ): List<CollectedCard> = symbols
        .distinct()
        .filterNot { profile.hasCard(roomIndex, it) }
        .map { CollectedCard(roomIndex, it) }

    fun completesSet(profile: PlayerProfile, roomIndex: Int, granted: List<CollectedCard>): Boolean {
        if (profile.paidSetBonuses.contains(roomIndex)) return false
        val owned = profile.cards + granted.map { PlayerProfile.cardKey(it.roomIndex, it.symbol) }
        return owned.count { it.startsWith("$roomIndex:") } >= GameConfig.CARDS_PER_ROOM
    }
}
