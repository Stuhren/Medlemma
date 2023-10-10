package com.example.medlemma.View

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardActions.Companion.Default
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medlemma.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMemberships() {
    // Get a reference to the ViewModel
    val viewModel: MyMembershipsViewModel = viewModel()

    // Observe data from the ViewModel
    val categories by viewModel.fetchCategories().observeAsState(initial = emptyList())
    val logos by viewModel.fetchLogos().observeAsState(initial = emptyList())

    var expanded by remember { mutableStateOf(false) }
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
                        .clip(shape = CustomShapes.medium)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(4.dp, SoftGray, shape = CustomShapes.medium) // Add a border with SoftGray color
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




