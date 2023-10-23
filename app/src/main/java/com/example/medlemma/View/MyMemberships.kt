package com.example.medlemma.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.medlemma.ViewModel.MyMembershipsViewModel
import com.example.medlemma.ui.theme.CustomShapes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import com.example.medlemma.Model.FirebaseRepository
import com.example.medlemma.ui.theme.DarkGray
import com.example.medlemma.ui.theme.MedlemmaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMemberships(email: String?) {
    MedlemmaTheme {
        val viewModel: MyMembershipsViewModel = viewModel()

        val view = LocalContext.current

        val members by viewModel.fetchAllMembers().observeAsState(initial = emptyList())
        val companies by viewModel.fetchAllCompanies().observeAsState(initial = emptyList())

        val currentMember = members.find { it.email == email }

        // Access the user's current memberships
        val currentMemberships = currentMember?.currentMemberships ?: emptyList()

        // Filter companies to show only those the user is a member of
        val userCompanies = companies.filter { currentMemberships.contains(it.id) }

        val categories = companies.map { it.category }
        val id = companies.map { it.id }
        var expanded by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }
        var selectedCategory by remember { mutableStateOf("Category") } // Set "Alla" as the initial category
        var selectedId by remember { mutableStateOf(id.firstOrNull()) }
        var QrCode = members.map { it.identificationURL }

        var filteredCompanies = remember { mutableStateOf(userCompanies) }

        DisposableEffect(selectedCategory) {
            filteredCompanies.value = if (selectedCategory == "Alla" || selectedCategory == "Category") {
                userCompanies
            } else {
                userCompanies.filter { it.category == selectedCategory }
            }

            onDispose {} // Dispose the effect
        }

        Box(modifier = Modifier.fillMaxSize()) {
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
                            Text(text = selectedCategory)
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

                            companies.forEach { item ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedCategory = item.category
                                        expanded = false
                                    }
                                ) {
                                    Text(text = item.category)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Display logos in clickable cards with a soft gray background and border
                items(userCompanies) { item ->
                    if (selectedCategory == "Alla" || selectedCategory == "Category" || item.category == selectedCategory) {
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
                                        selectedId = item.id
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
                                            .border(
                                                4.dp,
                                                DarkGray,
                                                shape = CustomShapes.medium
                                            )
                                    )
                                }
                            }
                        }
                    }
                }

            }

            // Overlay for darkening effect
            if (showDialog) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)) // Adjust alpha for desired darkness
                        .clickable { /* This could close the dialog if needed */ }
                )
            }

            // Dialog outside the LazyColumn
            if (showDialog && selectedId != null) {
                val item = userCompanies.find { it.id == selectedId }
                if (currentMember != null && item != null) {
                    SimpleDialog(
                        Category = item.category,
                        Qr = currentMember.identificationURL,
                        logo = item.companyLogo,
                        Name = item.companyName,
                        onDismiss = {
                            showDialog = false
                            selectedId = null
                        },
                        onDelete = {
                            if (email != null) {
                                viewModel.deleteMembership(email = email, companyId = selectedId!!)
                            }
                            showDialog = false
                        }
                    )
                }
            }
        }
    }
}