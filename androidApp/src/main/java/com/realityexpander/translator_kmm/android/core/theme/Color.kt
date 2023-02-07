package com.realityexpander.translator_kmm.android.core.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.realityexpander.translator_kmm.core.presentation.SharedColors

val AccentViolet = Color(SharedColors.AccentViolet)
val LightBlue = Color(SharedColors.LightBlue)
val LightBlueGrey = Color(SharedColors.LightBlueGrey)
val TextBlack = Color(SharedColors.TextBlack)
val DarkGrey = Color(SharedColors.DarkGrey)

val lightColors = lightColors(
    primary = AccentViolet,
    background = LightBlueGrey,
    onPrimary = Color.White,
    onBackground = TextBlack,
    surface = Color.White,
    onSurface = TextBlack
)

val darkColors = darkColors(
    primary = AccentViolet,
    background = DarkGrey,
    onPrimary = Color.White,
    onBackground = Color.White,
    surface = DarkGrey,
    onSurface = Color.White
)