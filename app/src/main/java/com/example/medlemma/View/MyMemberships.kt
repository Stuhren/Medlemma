package com.example.medlemma.View

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.medlemma.ViewModel.MyMembershipsViewModel
import com.example.medlemma.ui.theme.SoftGray
import com.example.medlemma.ui.theme.CustomShapes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import com.example.medlemma.Model.FirebaseRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMemberships() {
    // Get a reference to the ViewModel
    val viewModel: MyMembershipsViewModel = viewModel()

    // Observe data from the ViewModel
    val data by viewModel.fetchAllData().observeAsState(initial = emptyList())
    val view = LocalContext.current
    val categories = data.map { it.category }
    val logos = data.map { it.companyLogo }

    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull()) }

    // The main composable
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                TextButton(
                    onClick = { expanded = true }
                ) {
                    Text(text = selectedCategory ?: "Category")

                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null,)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {

                    // Add the "All" option first
                    DropdownMenuItem(
                        onClick = {
                            selectedCategory = "Alla"
                            expanded = false
                        }
                    ) {
                        Text(text = "Alla")
                    }

                    categories.forEach { category ->
                        DropdownMenuItem(
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        ) {
                            Text(text = category)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }


        // Display logos in clickable cards with a soft gray background and border
        items(data) { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            showDialog = true
                        }
                        .clip(shape = CustomShapes.medium)
                ) {

                    if (showDialog) {
                        SimpleDialog(
                            Category = item.category,
                            logo = item.companyLogo,
                            Name = item.companyName
                        ) {
                            showDialog = false // Close the dialog when needed
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                4.dp,
                                SoftGray,
                                shape = CustomShapes.medium
                            ) // Add a border with SoftGray color
                    ) {
                        Image(
                            painter = rememberImagePainter(data = item.companyLogo),
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





