package fhnw.ws6c.theapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppDarkColors = darkColors(
    //Background colors
    primary = primaryDark,
    secondary = secondaryDark,
    surface = backContrastDark,
    onSurface = Color.White,
    background = Color.Black,
    primaryVariant = contrast1Dark,
    secondaryVariant = contrast2Dark

)

private val AppLightColors = lightColors(
    //Background colors
    primary = primaryLight,
    secondary = secondaryLight,
    surface = backContrastLight,
    onSurface = Color.Black,
    background = Color.White,
    primaryVariant = contrast1Light,
    secondaryVariant = contrast2Light
)

@Composable
fun WorkshopSixAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        AppDarkColors
    } else {
        AppLightColors
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        content = content
    )
}