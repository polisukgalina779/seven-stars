package com.Vugfeve7.Hidran.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val SPINS_PER_RUN = 3
    const val START_COINS = 1000
    const val REFILL_COINS = 500
    const val REELS = 3
    const val ROWS = 3
    const val STRIP_LENGTH = 10
    const val EVALUATE_MS = 400L
    const val WIN_CELEBRATION_MS = 900L
    const val RUN_COMPLETE_MS = 700L
    const val BIG_WIN_MULTIPLIER = 15
    const val LOSS_STREAK_FOR_TILT = 5
    const val WILD_TILT_PERCENT = 140
    const val TOTAL_ROOMS = 6
    const val TOTAL_STARS = 18
    const val CARDS_PER_ROOM = 5
    const val TOTAL_CARDS = 30
    const val ROOM_SET_BONUS_PERCENT = 60
    const val SEED_ROOM_FACTOR = 7919L
    const val SEED_RUN_FACTOR = 104729L
    val REEL_STOP_MS = listOf(900L, 1250L, 1600L)
    val REEL_REVOLUTIONS = listOf(3, 4, 5)
    val STREAK_PERCENT = listOf(100, 150, 200)
}
