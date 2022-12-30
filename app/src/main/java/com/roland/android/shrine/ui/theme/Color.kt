package com.roland.android.shrine.ui.theme

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val ShrinePink10 = Color(0xfffffbfa)
val ShrinePink50 = Color(0xfffeeae6)
val ShrinePink100 = Color(0xfffedbd0)
val ShrinePink200 = Color(0xfffff0ea)
val ShrinePink500 = Color(0xfffbb1ae)
val ShrinePink900 = Color(0xff442c2e)
val Error = Color(0xffc5032b)

internal val LightColorPalette = lightColors(
    primary =  ShrinePink100,
    primaryVariant =  ShrinePink500,
    secondary =  ShrinePink50,
    background =  ShrinePink100,
    surface =  ShrinePink10,
    error =  Error,
    onPrimary =  ShrinePink900,
    onSecondary =  ShrinePink900,
    onBackground =  ShrinePink900,
    onSurface =  ShrinePink900,
    onError =  ShrinePink10
)