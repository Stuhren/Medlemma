package com.example.medlemma.ViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.medlemma.ui.theme.MedlemmaTheme
import com.example.medlemma.ui.theme.RedTest
import com.example.medlemma.ui.theme.SoftGray

@ExperimentalMaterial3Api
@Composable
fun AppBar(onNavigationIconClick: () -> Unit) {
    androidx.compose.material.TopAppBar(
        backgroundColor = SoftGray,
        title = {
            Text(
                text = "Medlemma",
                style = MaterialTheme.typography.titleLarge.copy(color = Color.Black)
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer menu"
                )
            }
        }
    )
}

