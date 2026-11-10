package com.Vugfeve7.Hidran.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SevenStarsScheme = darkColorScheme(
    primary = AccentGold,
    onPrimary = BgBase,
    primaryContainer = AccentGoldLight,
    onPrimaryContainer = BgBase,
    secondary = AccentChrome,
    onSecondary = BgBase,
    tertiary = AccentCrimson,
    onTertiary = TextPrimary,
    background = BgBase,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceAlt,
    onSurfaceVariant = TextSecondary,
    outline = DividerLine,
    outlineVariant = DividerLine,
    error = AccentCrimson,
    onError = TextPrimary,
    scrim = BgBase
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SevenStarsScheme,
        typography = AppTypography,
        content = content
    )
}
