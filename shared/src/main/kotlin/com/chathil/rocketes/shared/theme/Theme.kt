package com.chathil.rocketes.shared.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

@Composable
fun RocketesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dimensions: RocketesDimension = RocketesDimension(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = {
            CompositionLocalProvider(
                LocalRocketesDimensions provides dimensions,
            ) {
                content()
            }
        }
    )
}

object RocketesTheme {

    val dimensions: RocketesDimension
        @Composable
        get() = LocalRocketesDimensions.current
}