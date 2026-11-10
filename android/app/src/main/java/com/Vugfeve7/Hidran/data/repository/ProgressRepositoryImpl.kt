package com.Vugfeve7.Hidran.data.repository

import com.Vugfeve7.Hidran.core.config.GameConfig
import com.Vugfeve7.Hidran.data.local.PreferencesStore
import com.Vugfeve7.Hidran.domain.model.PlayerProfile
import com.Vugfeve7.Hidran.domain.model.RiskMode
import com.Vugfeve7.Hidran.domain.model.RunResult
import com.Vugfeve7.Hidran.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepositoryImpl(
    private val store: PreferencesStore
) : ProgressRepository {

    private val _profile = MutableStateFlow(load())
    override val profile: StateFlow<PlayerProfile> = _profile.asStateFlow()

    private val _lastRun = MutableStateFlow<RunResult?>(null)
    override val lastRun: StateFlow<RunResult?> = _lastRun.asStateFlow()

    override fun update(transform: (PlayerProfile) -> PlayerProfile) {
        val updated = transform(_profile.value)
        _profile.value = updated
        persist(updated)
    }

    override fun publishRun(result: RunResult) {
        _lastRun.value = result
    }

    override fun clearRun() {
        _lastRun.value = null
    }

    override fun reset() {
        store.wipe()
        val fresh = defaultProfile()
        _profile.value = fresh
        _lastRun.value = null
        persist(fresh)
    }

    private fun load(): PlayerProfile = PlayerProfile(
        coins = store.readInt(KEY_COINS, GameConfig.START_COINS),
        totalSpins = store.readInt(KEY_SPINS, 0),
        bestWin = store.readInt(KEY_BEST_WIN, 0),
        unlockedRooms = decodeInts(store.readText(KEY_ROOMS, "0")),
        roomStars = decodeIntMap(store.readText(KEY_STARS, "")),
        cards = decodeSet(store.readText(KEY_CARDS, "")),
        paidSetBonuses = decodeInts(store.readText(KEY_BONUSES, "")),
        modeSpins = decodeModeMap(store.readText(KEY_MODE_SPINS, "")),
        modeBestRun = decodeModeMap(store.readText(KEY_MODE_BEST, "")),
        activeRoom = store.readInt(KEY_ACTIVE_ROOM, 0)
    )

    private fun persist(profile: PlayerProfile) {
        store.writeInt(KEY_COINS, profile.coins)
        store.writeInt(KEY_SPINS, profile.totalSpins)
        store.writeInt(KEY_BEST_WIN, profile.bestWin)
        store.writeText(KEY_ROOMS, profile.unlockedRooms.joinToString(SEPARATOR))
        store.writeText(KEY_STARS, encodeIntMap(profile.roomStars))
        store.writeText(KEY_CARDS, profile.cards.joinToString(SEPARATOR))
        store.writeText(KEY_BONUSES, profile.paidSetBonuses.joinToString(SEPARATOR))
        store.writeText(KEY_MODE_SPINS, encodeModeMap(profile.modeSpins))
        store.writeText(KEY_MODE_BEST, encodeModeMap(profile.modeBestRun))
        store.writeInt(KEY_ACTIVE_ROOM, profile.activeRoom)
    }

    private fun defaultProfile(): PlayerProfile = PlayerProfile(
        coins = GameConfig.START_COINS,
        totalSpins = 0,
        bestWin = 0,
        unlockedRooms = setOf(0),
        roomStars = emptyMap(),
        cards = emptySet(),
        paidSetBonuses = emptySet(),
        modeSpins = emptyMap(),
        modeBestRun = emptyMap(),
        activeRoom = 0
    )

    private fun encodeIntMap(source: Map<Int, Int>): String =
        source.entries.joinToString(SEPARATOR) { entry -> entry.key.toString() + PAIR + entry.value }

    private fun encodeModeMap(source: Map<RiskMode, Int>): String =
        source.entries.joinToString(SEPARATOR) { entry -> entry.key.name + PAIR + entry.value }

    private fun decodeSet(raw: String): Set<String> =
        raw.split(SEPARATOR).filter { it.isNotBlank() }.toSet()

    private fun decodeInts(raw: String): Set<Int> =
        raw.split(SEPARATOR).mapNotNull { it.trim().toIntOrNull() }.toSet()

    private fun decodeIntMap(raw: String): Map<Int, Int> = raw.split(SEPARATOR)
        .mapNotNull { entry ->
            val parts = entry.split(PAIR)
            val key = parts.getOrNull(0)?.toIntOrNull()
            val value = parts.getOrNull(1)?.toIntOrNull()
            if (key == null || value == null) null else key to value
        }
        .toMap()

    private fun decodeModeMap(raw: String): Map<RiskMode, Int> = raw.split(SEPARATOR)
        .mapNotNull { entry ->
            val parts = entry.split(PAIR)
            val key = RiskMode.entries.firstOrNull { it.name == parts.getOrNull(0) }
            val value = parts.getOrNull(1)?.toIntOrNull()
            if (key == null || value == null) null else key to value
        }
        .toMap()

    companion object {
        private const val SEPARATOR = ","
        private const val PAIR = ":"
        private const val KEY_COINS = "coins"
        private const val KEY_SPINS = "total_spins"
        private const val KEY_BEST_WIN = "best_win"
        private const val KEY_ROOMS = "unlocked_rooms"
        private const val KEY_STARS = "room_stars"
        private const val KEY_CARDS = "cards"
        private const val KEY_BONUSES = "paid_bonuses"
        private const val KEY_MODE_SPINS = "mode_spins"
        private const val KEY_MODE_BEST = "mode_best"
        private const val KEY_ACTIVE_ROOM = "active_room"
    }
}
