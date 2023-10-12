package com.example.medlemma.ui.theme

import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val DefaultColorScheme = lightColorScheme(

    // Default state button and top bar background
    primary = SoftGray,

    // This color is used for secondary elements in your app, such as secondary buttons and text
    secondary = Blue,

    tertiary = RedTest,

    /* You can override other default colors here, like:
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
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
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = DefaultColorScheme,
        typography = Typography,
        content = content,
        shapes = CustomShapes
    )
}