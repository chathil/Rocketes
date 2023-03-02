package com.astro.rocketapp.shared.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class RocketesDimension(
    val spacingXXS: Dp = 2.dp,
    val spacingXS: Dp = 4.dp,
    val spacingSmall: Dp = 8.dp,
    val spacingMedium: Dp = 16.dp,
    val spacingLarge: Dp = 24.dp,
    val spacingXL: Dp = 32.dp,
    val spacingXXL: Dp = 48.dp,
)

internal val LocalRocketesDimensions = staticCompositionLocalOf<RocketesDimension> {
    error("No TunggalDimensions provided")
}