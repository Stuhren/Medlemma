package com.example.medlemma.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.medlemma.ViewModel.MyMembershipsViewModel
import com.example.medlemma.ui.theme.CustomShapes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import com.example.medlemma.ui.theme.DarkGray
import com.example.medlemma.ui.theme.MedlemmaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseMemberships() {
    MedlemmaTheme {
        val viewModel: MyMembershipsViewModel = viewModel()
        val companies by viewModel.fetchAllCompanies().observeAsState(initial = emptyList())

        var searchQuery by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {searchQuery = it},
                    label = { Text(text = "Search Memberships")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                val filteredCompanies = companies.filter {
                    it.companyName.contains(searchQuery, ignoreCase = true)
                }

                LazyColumn {
                items(filteredCompanies) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
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
                                    )
                            ) {
                                Image(
                                    painter = rememberImagePainter(data = item.companyLogo),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .height(220.dp)
                                )
                            }
                            }
                        }
                    }
                }
            }
        }
    }
}
