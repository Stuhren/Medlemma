package com.example.medlemma.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.medlemma.ui.theme.SoftGray

@Composable
fun MyMemberships() {
    // Get a reference to the ViewModel
    val viewModel: MyMembershipsViewModel = viewModel()

    // Observe data from the ViewModel
    val categories by viewModel.fetchCategories().observeAsState(initial = emptyList())
    val logos by viewModel.fetchLogos().observeAsState(initial = emptyList())

    // The main composable
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "MY MEMBERSHIPS",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Text(
                text = "Categories: ${categories.joinToString(", ")}",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Display logos in clickable cards with a soft gray background and border
        items(logos) { logoUrl ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            // Handle card click here
                            // You can navigate to a detailed view or perform other actions
                        }
                        .background(SoftGray) // Set the background color to SoftGray
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, SoftGray) // Add a border with SoftGray color
                    ) {
                        Image(
                            painter = rememberImagePainter(data = logoUrl),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .height(225.dp) // Adjust the height as needed
                        )
                    }
                }
            }
        }
    }
}




