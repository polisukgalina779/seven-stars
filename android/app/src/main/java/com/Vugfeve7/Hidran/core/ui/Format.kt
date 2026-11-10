package com.Vugfeve7.Hidran.core.ui

import com.Vugfeve7.Hidran.R
import com.Vugfeve7.Hidran.domain.model.ReelSpeed
import com.Vugfeve7.Hidran.domain.model.RiskMode
import com.Vugfeve7.Hidran.domain.model.SlotSymbol
import kotlin.math.abs

fun formatNumber(value: Int): String {
    val digits = abs(value).toString()
    val builder = StringBuilder()
    digits.forEachIndexed { index, character ->
        if (index > 0 && (digits.length - index) % 3 == 0) builder.append(',')
        builder.append(character)
    }
    return if (value < 0) "-" + builder.toString() else builder.toString()
}

fun streakLabel(percent: Int): String = when (percent) {
    200 -> "2"
    150 -> "1.5"
    else -> "1"
}

fun symbolDrawable(symbol: SlotSymbol): Int = when (symbol) {
    SlotSymbol.COMMON -> R.drawable.sprite_1
    SlotSymbol.BRIGHT -> R.drawable.sprite_2
    SlotSymbol.RARE -> R.drawable.sprite_3
    SlotSymbol.PRIME -> R.drawable.sprite_4
    SlotSymbol.WILD -> R.drawable.sprite_5
}

fun symbolTierRes(symbol: SlotSymbol): Int = when (symbol) {
    SlotSymbol.COMMON -> R.string.tier_common
    SlotSymbol.BRIGHT -> R.string.tier_bright
    SlotSymbol.RARE -> R.string.tier_rare
    SlotSymbol.PRIME -> R.string.tier_prime
    SlotSymbol.WILD -> R.string.tier_wild
}

fun roomNameRes(index: Int): Int = when (index) {
    0 -> R.string.room_1
    1 -> R.string.room_2
    2 -> R.string.room_3
    3 -> R.string.room_4
    4 -> R.string.room_5
    else -> R.string.room_6
}

fun modeNameRes(mode: RiskMode): Int = when (mode) {
    RiskMode.CALM -> R.string.mode_calm
    RiskMode.BOLD -> R.string.mode_bold
    RiskMode.WILD -> R.string.mode_wild
}

fun speedNameRes(speed: ReelSpeed): Int = when (speed) {
    ReelSpeed.RELAXED -> R.string.speed_relaxed
    ReelSpeed.STANDARD -> R.string.speed_standard
    ReelSpeed.RAPID -> R.string.speed_rapid
}
