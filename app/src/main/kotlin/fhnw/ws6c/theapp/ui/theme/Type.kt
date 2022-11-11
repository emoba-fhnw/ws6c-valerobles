package fhnw.ws6c.theapp.ui.theme


import androidx.compose.material.Typography
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import fhnw.ws6c.R


private val Abel = FontFamily(
    Font(
        R.font.abel_regular,
    ),
)

val typography = Typography(
    defaultFontFamily = Abel,
    h1 = TextStyle(
        letterSpacing = 1.15.sp,
        fontSize = 32.sp
    ),
    h2 = TextStyle(
        letterSpacing = 1.15.sp,
        fontSize = 20.sp
    ),
    h3 = TextStyle(
        letterSpacing = 1.15.sp,
        fontSize = 18.sp
    ),
    h4 = TextStyle(
        letterSpacing = 1.15.sp,
        fontSize = 12.sp
    )
)