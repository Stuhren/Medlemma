package com.example.medlemma.View

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medlemma.ui.theme.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.example.medlemma.Model.ViewCompany
import com.example.medlemma.Model.addCompanyToFirebase
import com.example.medlemma.Model.generateCustomID
import com.example.medlemma.Model.updateCompanyInFirebase
import com.example.medlemma.Model.uploadImageToFirebaseStorage
import com.example.medlemma.ViewModel.CompanyViewModel
import java.util.UUID

@Composable
fun AdminDashboard(companyViewModel: CompanyViewModel) {


    MedlemmaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AddCompany()
                CompanyScreen(viewModel = companyViewModel)
            }
        }
    }
}

enum class IconState {
    NOT_COMPLETED, COMPLETED
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun AddCompany(modifier: Modifier = Modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val iconState1 = remember { mutableStateOf(IconState.NOT_COMPLETED) }
    val iconState2 = remember { mutableStateOf(IconState.NOT_COMPLETED) }
    var downloadUrl by remember { mutableStateOf<String?>(null) }
    var newCompanyId by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val imageName = "your_image_name_${UUID.randomUUID()}.jpg" // Generate a unique name for the image
            uploadImageToFirebaseStorage(uri, imageName) { imageUrl ->
                if (imageUrl != null) {
                    downloadUrl = imageUrl
                    iconState2.value = IconState.COMPLETED
                } else {
                    // Handle the upload error
                }
            }
        }
    }

    Column(
        modifier = modifier.padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Company",
            fontSize = 22.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 30.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        val categoryState = rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = categoryState.value,
            onValueChange = { categoryState.value = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
        )
        Spacer(modifier = Modifier.height(10.dp))

        val nameState = rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text("Company Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
        )
        Spacer(modifier = Modifier.height(10.dp))

        val registerUrlState = rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = registerUrlState.value,
            onValueChange = { registerUrlState.value = it },
            label = { Text("Register Url") },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                generateCustomID{ customID ->
                    if (customID != null) {
                        newCompanyId = customID
                        iconState1.value = IconState.COMPLETED
                    } else {
                        println("Failed to retrieve company ID from Firebase.")
                    }
                }
            }) {
                Text("Fetch Company ID")
                Spacer(modifier = Modifier.padding(start = 4.dp))
                when (iconState1.value) {
                    IconState.NOT_COMPLETED -> {
                        CustomIcon(Icons.Outlined.Clear, Color.Red, xOffset = 0.dp, yOffset = 0.dp)
                    }

                    IconState.COMPLETED -> {
                        CustomIcon(Icons.Outlined.CheckCircle, Color.Green, xOffset = 0.dp, yOffset = 0.dp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                launcher.launch("image/*")
            }) {
                Text("Upload Company Logo")
                Spacer(modifier = Modifier.padding(start = 4.dp))
                when (iconState2.value) {
                    IconState.NOT_COMPLETED -> {
                        CustomIcon(Icons.Outlined.Clear, Color.Red, xOffset = 0.dp, yOffset = 0.dp)
                    }
                    IconState.COMPLETED -> {
                        CustomIcon(Icons.Outlined.CheckCircle, Color.Green, xOffset = 0.dp, yOffset = 0.dp)
                    }
                }
            }


            Button(onClick = {
                // Check if newCompanyId is not null before using it
                newCompanyId?.let { companyId ->
                    addCompanyToFirebase(
                        categoryState.value,
                        nameState.value,
                        registerUrlState.value,
                        companyId.toString(),
                        downloadUrl ?: ""
                    ) { success ->
                        if (success) {
                            Toast.makeText(context, "Added a company to the database", Toast.LENGTH_SHORT).show()
                        } else {
                            // Failed to add the company, handle the error
                            Toast.makeText(context, "Failed to add to the database", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }) {
                Text("Add")
            }
        }
    }
}


@Composable
fun CustomIcon(icon: ImageVector, color: Color, xOffset: Dp, yOffset: Dp) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color,
        modifier = Modifier.size(30.dp) then  Modifier.offset(x = -xOffset, y = yOffset)
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun CompanyView(viewModel: CompanyViewModel) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Divider(color = Color.Gray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(30.dp))

        // REGISTERED USERS
        Text(
            text = "Update Companies",
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        // SEARCH FIELD
        val searchState = rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search (Company Name)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
        )
    }



    Spacer(modifier = Modifier.height(20.dp))

    // Display the list of companies
    CompanyList(viewModel, searchQuery)

    Spacer(modifier = Modifier.height(60.dp))
}


@Composable
private fun CompanyList(viewModel: CompanyViewModel, searchQuery: String) {
    val companyData by viewModel.companyData.collectAsState()

    Column {
        // Display company data and "Delete" button for each company
        for (company in companyData) {
            // Filter companies based on the search query
            if (company.companyName.contains(searchQuery, true)) {
                CompanyItem(company = company) {
                    // Implement the delete logic in the ViewModel
                    viewModel.deleteCompany(company)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyItem(company: ViewCompany, onDelete: () -> Unit) {
    // Create text field values for each field
    var companyName by remember { mutableStateOf(company.companyName) }
    var category by remember { mutableStateOf(company.category) }
    var registerUrl by remember { mutableStateOf(company.registerUrl) }
    var companyLogo by remember { mutableStateOf(company.companyLogo) }
    val iconState3 = remember { mutableStateOf(IconState.NOT_COMPLETED) }
    var downloadUrl by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val imageName = "your_image_name_${UUID.randomUUID()}.jpg" // Generate a unique name for the image
            uploadImageToFirebaseStorage(uri, imageName) { imageUrl ->
                if (imageUrl != null) {
                    downloadUrl = imageUrl
                    companyLogo = imageUrl
                    iconState3.value = IconState.COMPLETED
                } else {
                    // Handle the upload error
                }
            }
        }
    }

    val textFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = SoftGray,
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(DarkGray)
            .clickable { /* Handle item click if needed */ }
    ) {
        Text(
            text = "Company ID: ${company.id}",
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )

        // Display editable text fields
        TextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Company Name") },
            colors = textFieldColors,
            modifier = Modifier
                .padding(8.dp)
        )

        TextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            colors = textFieldColors,
            modifier = Modifier
                .padding(8.dp)
        )

        TextField(
            value = registerUrl,
            onValueChange = { registerUrl = it },
            label = { Text("Register URL") },
            colors = textFieldColors,
            modifier = Modifier
                .padding(8.dp)
        )

        TextField(
            value = companyLogo,
            onValueChange = { companyLogo = it },
            label = { Text("Company Logo") },
            colors = textFieldColors,
            modifier = Modifier
                .padding(8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                launcher.launch("image/*")
            },
                colors =ButtonDefaults.buttonColors(SoftGray, contentColor = Color.Black),
                modifier = Modifier.padding(8.dp)
                ) {
                Text("Upload New Company Logo")
                Spacer(modifier = Modifier.padding(start = 4.dp))
                when (iconState3.value) {
                    IconState.NOT_COMPLETED -> {
                        CustomIcon(Icons.Outlined.Clear, Color.Red, xOffset = 0.dp, yOffset = 0.dp)
                    }

                    IconState.COMPLETED -> {
                        CustomIcon(
                            Icons.Outlined.CheckCircle,
                            Color.Green,
                            xOffset = 0.dp,
                            yOffset = 0.dp
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Delete")
            }

            Button(
                onClick = {
                    // Perform the update action here
                    updateCompanyInFirebase(
                        company.id,  // Pass the ID of the company
                        category,
                        companyName,
                        registerUrl,
                        companyLogo
                    ) { success ->
                        if (success) {
                            Toast.makeText(context, "Updated the company", Toast.LENGTH_LONG).show()
                        } else {
                            // Failed to add the company, handle the error
                            Toast.makeText(context, "Failed to update the company", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(blue2),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Update")
            }
        }


        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun CompanyScreen(viewModel: CompanyViewModel) {
    val companyModel = viewModel.companyModel

    DisposableEffect(Unit) {
        // Start listening to the database when the Composable is first launched
        companyModel.startListening()

        onDispose {
            // Stop listening when the Composable is disposed
            companyModel.stopListening()
        }
    }

    CompanyView(viewModel)
}