package com.example.medlemma.ui.theme

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val DefaultColorScheme = lightColorScheme(
    primary = DarkGray,
    secondary = Blue,
    tertiary = RedTest,
    background = SoftGray
)

val CustomShapes = Shapes(
    small = RoundedCornerShape(10.dp), // Adjust the radius as needed
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(40.dp)
)

@Composable
fun MedlemmaTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DefaultColorScheme.primary.toArgb()
            window.setBackgroundDrawable(ColorDrawable(RedTest.toArgb())) // Set window background color to SoftGray
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    Box(modifier = Modifier.background(DefaultColorScheme.background)) {
        MaterialTheme(
            colorScheme = DefaultColorScheme,
            typography = Typography,
            content = content,
            shapes = CustomShapes
        )
    }
}