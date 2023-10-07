package com.example.medlemma.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medlemma.R
import com.example.medlemma.ViewModel.SigninViewModel
import com.example.medlemma.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavController, viewModel: SigninViewModel, signInAction: (email: String, pass: String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val errorMessage by viewModel.errorMessage.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login", fontSize = 34.sp, modifier = Modifier.align(Alignment.Start))
            Text(text = "Please sign in to continue.", fontSize = 16.sp, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(24.dp))

            val emailState = rememberSaveable { mutableStateOf("") }
            val passwordState = rememberSaveable { mutableStateOf("") }

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
            )
            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
                    visualTransformation = PasswordVisualTransformation(),
                    trailingIcon = {
                        Box(
                            Modifier.clickable(onClick = { /* Navigate to forgot password screen or perform other actions */ }),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = "Forgot?",
                                style = MaterialTheme.typography.bodyLarge.copy(color = SoftGray),
                                modifier = Modifier.padding(end = 8.dp) // Adjust as needed
                            )
                        }
                    }
                )

            }


            Spacer(modifier = Modifier.height(26.dp))

            Button(modifier = Modifier.align(Alignment.End), onClick = {
                signInAction(emailState.value, passwordState.value)
            }) {
                Text("LOGIN")
            }

            Spacer(modifier = Modifier.height(64.dp))


            TextButton(
                onClick = { /*TODO*/ },
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start // Align to the start
                ) {
                    Image(painter = painterResource(id = R.drawable.icons8_google), contentDescription = "", modifier = Modifier.size(24.dp))
                    Text("Sign up with Google", modifier = Modifier.padding(start = 5.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            TextButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(start = 15.dp),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start // Align to the start
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_facebook), contentDescription = "")
                    Text("Sign up with Facebook", modifier = Modifier.padding(start = 5.dp))
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account? ")

                val signUpText = "Sign up"
                val signUpString = AnnotatedString(signUpText)

                ClickableText(
                    text = signUpString,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Blue),
                    onClick = {
                        navController.navigate("signUp")
                    }
                )
            }

            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(8.dp)
                        .padding(top = 40.dp) // Ensure this is above the "Sign up" text.
                )
            }
        }

    }
}
