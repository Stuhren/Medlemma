package com.example.medlemma.View

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun AddCompany(modifier: Modifier = Modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current
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
                //addCompanyLogoAction(categoryState.value, nameState.value, registerurlState.value)
            }) {
                Text("Upload Company Logo")
            }

            Button(onClick = {
                //addCompanyAction(categoryState.value, nameState.value, registerurlState.value)
            }) {
                Text("Add")
            }
        }
    }
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
                .padding(top = 60.dp)
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
