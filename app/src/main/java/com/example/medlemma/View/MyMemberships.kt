package com.example.medlemma.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.medlemma.ViewModel.MyMembershipsViewModel

@Composable
fun MyMemberships() {
    // Get a reference to the ViewModel
    val viewModel: MyMembershipsViewModel = viewModel()

    // Observe data from the ViewModel
    val categories by viewModel.fetchCategories().observeAsState(initial = emptyList())
    val logos by viewModel.fetchLogos().observeAsState(initial = emptyList())

    // The main composable
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "MY MEMBERSHIPS",
            fontSize = 100.sp,
            lineHeight = 116.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Categories: ${categories.joinToString(", ")}",
            fontSize = 36.sp,
            modifier = Modifier.padding(16.dp).align(alignment = Alignment.End)
        )

        // Display logos
        logos.forEach { logoUrl ->
            Image(
                painter = rememberImagePainter(data = logoUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }
    }
}