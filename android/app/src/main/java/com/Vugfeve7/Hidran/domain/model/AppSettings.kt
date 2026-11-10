package com.Vugfeve7.Hidran.domain.model

enum class ReelSpeed(val multiplierPercent: Int) {
    RELAXED(125),
    STANDARD(100),
    RAPID(65)
}

data class AppSettings(
    val defaultMode: RiskMode = RiskMode.BOLD,
    val reelSpeed: ReelSpeed = ReelSpeed.STANDARD,
    val reduceMotion: Boolean = false,
    val showPayTable: Boolean = true
)
