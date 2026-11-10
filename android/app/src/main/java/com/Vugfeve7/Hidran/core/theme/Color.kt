package com.Vugfeve7.Hidran.core.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val BgBase = Color(0xFF0B0B0F)
val SurfaceCard = Color(0xFF141418)
val SurfaceAlt = Color(0xFF1C1C24)
val SurfaceWarm = Color(0xFF1E1A14)
val AccentGold = Color(0xFFD4AF37)
val AccentGoldLight = Color(0xFFF5D77A)
val AccentCrimson = Color(0xFFC8102E)
val AccentChrome = Color(0xFF8FA9C4)
val TextPrimary = Color(0xFFF6F1E4)
val TextSecondary = Color(0xFFA89C82)
val TextMuted = Color(0xFF6E6657)
val WinGreen = Color(0xFF4CC38A)
val DividerLine = Color(0xFF2A2A33)
val GlossWhite = Color(0x14FFFFFF)

object Palette {

    val chromeRim = Brush.linearGradient(listOf(AccentGoldLight, AccentGold, AccentChrome))

    val goldCta = Brush.linearGradient(listOf(AccentGold, AccentGoldLight, AccentCrimson))

    val goldTrack = Brush.horizontalGradient(listOf(AccentGold, AccentGoldLight))

    val gloss = Brush.verticalGradient(listOf(GlossWhite, Color.Transparent))

    val cellFill = Brush.verticalGradient(listOf(SurfaceAlt, SurfaceCard))

    val splashOverlay = listOf(
        BgBase.copy(alpha = 0.96f),
        SurfaceWarm.copy(alpha = 0.90f),
        BgBase.copy(alpha = 0.98f)
    )

    val menuOverlay = listOf(
        BgBase.copy(alpha = 0.74f),
        BgBase.copy(alpha = 0.38f),
        BgBase.copy(alpha = 0.88f)
    )

    val levelOverlay = listOf(
        BgBase.copy(alpha = 0.90f),
        SurfaceAlt.copy(alpha = 0.66f),
        BgBase.copy(alpha = 0.92f)
    )

    val gameOverlay = listOf(
        BgBase.copy(alpha = 0.88f),
        BgBase.copy(alpha = 0.58f),
        BgBase.copy(alpha = 0.94f)
    )

    val resultOverlay = listOf(
        SurfaceWarm.copy(alpha = 0.86f),
        BgBase.copy(alpha = 0.62f),
        BgBase.copy(alpha = 0.94f)
    )

    val collectionOverlay = listOf(
        BgBase.copy(alpha = 0.92f),
        SurfaceWarm.copy(alpha = 0.70f),
        BgBase.copy(alpha = 0.92f)
    )

    val progressOverlay = listOf(
        BgBase.copy(alpha = 0.93f),
        SurfaceAlt.copy(alpha = 0.74f),
        BgBase.copy(alpha = 0.90f)
    )

    val settingsOverlay = listOf(
        BgBase.copy(alpha = 0.95f),
        SurfaceCard.copy(alpha = 0.80f),
        BgBase.copy(alpha = 0.92f)
    )
}
