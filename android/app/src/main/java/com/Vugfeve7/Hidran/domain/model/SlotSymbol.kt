package com.Vugfeve7.Hidran.domain.model

enum class SlotSymbol(val payout: Int) {
    COMMON(4),
    BRIGHT(7),
    RARE(11),
    PRIME(28),
    WILD(70);

    val isWild: Boolean
        get() = this == WILD
}
