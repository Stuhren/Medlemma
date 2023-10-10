package com.example.medlemma.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medlemma.R
import com.example.medlemma.ui.theme.SoftGray

@Composable
fun AdminDashboard() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AddCompany()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun AddCompany(modifier: Modifier = Modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Add company
        Text(text = "Add Company", fontSize = 22.sp, modifier = Modifier.align(Alignment.Start).padding(top = 30.dp))
        Spacer(modifier = Modifier.height(10.dp))
        // Category

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
        // Name

        val nameState = rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = categoryState.value,
            onValueChange = { categoryState.value = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
        )
        Spacer(modifier = Modifier.height(10.dp))
        // RegisterUrl

        val registerurlState = rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = categoryState.value,
            onValueChange = { categoryState.value = it },
            label = { Text("Register Url") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(modifier = Modifier.align(Alignment.Start), onClick = {
            //addCompanyLogoAction(categoryState.value, nameState.value, registerurlState.value)
        }) {
            Text("Upload Company Logo")
        }



        Spacer(modifier = Modifier.height(10.dp))

        Button(modifier = Modifier.align(Alignment.End), onClick = {
            //addCompanyAction(categoryState.value, nameState.value, registerurlState.value)
        }) {
            Text("Add")
        }

    }
}