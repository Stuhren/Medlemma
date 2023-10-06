package com.example.medlemma.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.medlemma.R

val BalsamiqSans = FontFamily(
    Font(R.font.balsamiq_sans_regular) //
)

val Typography = Typography(
    // Define your custom text style using the Balsamiq Sans font
    bodyLarge = TextStyle(
        fontFamily = BalsamiqSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Add more text styles as needed
)
