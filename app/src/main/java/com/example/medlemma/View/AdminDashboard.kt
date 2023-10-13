package com.example.medlemma.View

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
import com.example.medlemma.ui.theme.RedTest
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.example.medlemma.Model.getCompanyCountFromFirebase
import com.google.android.play.integrity.internal.x

@Composable
fun AdminDashboard() {
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
                RegisteredUsers()
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
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
        )
        Spacer(modifier = Modifier.height(10.dp))

        val registerurlState = rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = registerurlState.value,
            onValueChange = { registerurlState.value = it },
            label = { Text("Register Url") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                getCompanyCountFromFirebase { companyCount ->
                    if (companyCount != -1) {
                        val newCompanyId = companyCount + 1
                        iconState1.value = IconState.COMPLETED
                    } else {
                        println("Failed to retrieve company ID from Firebase.")
                    }
                }
            }) {
                Text("Fetch Company ID")
            }


            when (iconState1.value) {
                IconState.NOT_COMPLETED -> {
                    CustomIcon(Icons.Outlined.Clear, Color.Red, xOffset = 167.dp, yOffset = 10.dp)
                }

                IconState.COMPLETED -> {
                    CustomIcon(Icons.Outlined.CheckCircle, Color.Green, xOffset = 167.dp, yOffset = 10.dp)
                }
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                //addCompanyLogoAction(categoryState.value, nameState.value, registerurlState.value)
                iconState2.value = IconState.COMPLETED
            }) {
                Text("Upload Company Logo")
            }

            when (iconState2.value) {
                IconState.NOT_COMPLETED -> {
                    CustomIcon(Icons.Outlined.Clear, Color.Red, xOffset = 32.dp, yOffset = 10.dp)
                }
                IconState.COMPLETED -> {
                    CustomIcon(Icons.Outlined.CheckCircle, Color.Green, xOffset = 32.dp, yOffset = 10.dp)
                }
            }

            Button(onClick = {
                //addCompanyAction(categoryState.value, nameState.value, registerurlState.value)
            }) {
                Text("Add")
            }
        }
    }
}

@Composable
private fun CustomIcon(icon: ImageVector, color: Color, xOffset: Dp, yOffset: Dp) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color,
        modifier = Modifier.size(30.dp) then  Modifier.offset(x = -xOffset, y = yOffset)
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun RegisteredUsers(modifier: Modifier = Modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = modifier.padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // REGISTERED USERS
        Text(
            text = "Registered Users",
            fontSize = 22.sp,
            modifier = Modifier
                .align(Alignment.Start)

        )

        // SEARCH FIELD
        val searchState = rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = searchState.value,
            onValueChange = { searchState.value = it },
            label = { Text("Search (Email, Created, UID, Signed in)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
        )


        // USERS
        Spacer(modifier = Modifier.height(30.dp))


        Divider(color = Color.Gray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(20.dp))

        // EMAIL
        Text(
            text = "Email",
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )

        Text(
            text = "test99@gmail.com",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // CREATED
        Text(
            text = "Created",
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Text(
            text = "Oct 6, 2023",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // USER UID
        Text(
            text = "User UID",
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Text(
            text = "HpW4mwMRlSbYoFQXqwsTFaJFec32",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // SIGNED IN
        Text(
            text = "Signed in",
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Text(
            text = "Oct 6, 2023",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(onClick = {
            //addCompanyAction(categoryState.value, nameState.value, registerurlState.value)
        },
            colors = ButtonDefaults.buttonColors(RedTest),
            modifier = Modifier.align(Alignment.Start)) {
            Text("Delete")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Divider(color = Color.Gray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(60.dp))
    }
}

