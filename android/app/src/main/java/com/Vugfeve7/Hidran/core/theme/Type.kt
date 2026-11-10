package com.Vugfeve7.Hidran.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 44.sp,
        lineHeight = 50.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 4.sp,
        color = TextPrimary
    ),
    displaySmall = TextStyle(
        fontSize = 32.sp,
        lineHeight = 38.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.sp,
        color = TextPrimary
    ),
    headlineMedium = TextStyle(
        fontSize = 26.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 2.sp,
        color = TextPrimary
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        lineHeight = 23.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        color = TextPrimary
    ),
    bodyLarge = TextStyle(
        fontSize = 15.sp,
        lineHeight = 21.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp,
        lineHeight = 15.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.4.sp,
        color = TextSecondary
    )
)
