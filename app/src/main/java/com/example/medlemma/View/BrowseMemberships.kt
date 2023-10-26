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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.medlemma.ViewModel.MyMembershipsViewModel
import com.example.medlemma.ui.theme.CustomShapes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.example.medlemma.Model.FirebaseRepository
import com.example.medlemma.Model.FirebaseRepository.addMembershipToCurrentMemberships
import com.example.medlemma.ui.theme.DarkGray
import com.example.medlemma.ui.theme.MedlemmaTheme
import com.example.medlemma.ui.theme.SoftGray
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseMemberships() {
    MedlemmaTheme {
        val viewModel: MyMembershipsViewModel = viewModel()
        val companies by viewModel.fetchAllCompanies().observeAsState(initial = emptyList())
        val customScope: CoroutineScope = MainScope()

        var searchQuery by remember { mutableStateOf("") }
        var showDialog by remember { mutableStateOf(false) }
        val id = companies.map { it.id }
        var selectedId by remember { mutableStateOf(id.firstOrNull()) }
        val user = FirebaseAuth.getInstance().currentUser // Assume you've authenticated the user

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            item {
                Spacer(modifier = Modifier.height(10.dp))
                // Search bar inside the LazyColumn
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(text = "Search Memberships") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = CustomShapes.medium
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            val filteredCompanies = companies.filter {
                it.companyName.contains(searchQuery, ignoreCase = true)
            }

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

        // Dialog code...
        if (showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable {
                        showDialog = false // Close the dialog when clicking outside
                    }
            )
            if (showDialog && selectedId != null) {
                val item = companies.find { it.id == selectedId }
                if (item != null) {
                    // AddDialog composable here (replace with your actual dialog code)
                    // You can replace the AddDialog with your actual dialog content
                    AddDialog(
                        Category = item.category,
                        logo = item.companyLogo,
                        Name = item.companyName,
                        onAddClicked = {
                            if (user != null) {
                                customScope.launch {
                                    val success = addMembershipToCurrentMemberships(user.email, item.id)
                                    if (success.equals(String())) {
                                        // Handle successful addition
                                    } else {
                                        // Handle failure to add
                                    }
                                    showDialog = false // Close the dialog
                                    selectedId = null
                                }
                            }
                        },
                        onDismiss = {
                            showDialog = false // Close the dialog when needed
                            selectedId = null // Reset the selected item
                        }
                    )
                }
            }
        }
    }
}
