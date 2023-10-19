package com.example.medlemma.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.medlemma.ViewModel.MyMembershipsViewModel
import com.example.medlemma.ui.theme.CustomShapes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.livedata.observeAsState
import com.example.medlemma.Model.CompanyModel
import com.example.medlemma.ui.theme.DarkGray
import com.example.medlemma.ui.theme.MedlemmaTheme

@Composable
fun BrowseMemberships() {
    MedlemmaTheme {
        val viewModel: MyMembershipsViewModel = viewModel()
        val companies by viewModel.fetchAllCompanies().observeAsState(initial = emptyList())

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Display all companies
                items(companies) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    // Handle click on a company if needed
                                }
                                .clip(shape = CustomShapes.medium)
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        4.dp,
                                        DarkGray,
                                        shape = CustomShapes.medium
                                    ) // Add a border with SoftGray color
                            ) {
                                Image(
                                    painter = rememberImagePainter(data = item.companyLogo),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .height(220.dp) // Adjust the height as needed
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
