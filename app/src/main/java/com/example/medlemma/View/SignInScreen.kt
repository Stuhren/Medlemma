package com.example.medlemma.View

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.graphics.painter.Painter
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.example.medlemma.ui.theme.CustomShapes
import com.example.medlemma.ui.theme.MedlemmaTheme




@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavController, viewModel: SigninViewModel, signInAction: (email: String, pass: String) -> Unit) {
    MedlemmaTheme {
        val keyboardController = LocalSoftwareKeyboardController.current
        val errorMessage by viewModel.errorMessage.observeAsState()
        val view = LocalContext.current
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
                val logo: Painter = painterResource(id = R.drawable.medlemmalogo)
                Image(
                    painter = logo,
                    contentDescription = "Medlemma Logo",
                    modifier = Modifier.size(150.dp)
                )
                Text(text = "Login", fontSize = 24.sp, modifier = Modifier.align(Alignment.Start))
                Text(text = "Please sign in to continue.", fontSize = 14.sp, modifier = Modifier.align(Alignment.Start))
                Spacer(modifier = Modifier.height(24.dp))

                val emailState = rememberSaveable { mutableStateOf("") }
                val passwordState = rememberSaveable { mutableStateOf("") }

                OutlinedTextField(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
                    shape = CustomShapes.large
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
                        shape = CustomShapes.large,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
                        visualTransformation = PasswordVisualTransformation(),
                        trailingIcon = {
                            Box(
                                Modifier.clickable(onClick = { /* Navigate to forgot password screen or perform other actions */ }),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                TextButton(onClick = {
                                    if(emailState.value.isNotEmpty()) {
                                        viewModel.resetPassword(emailState.value)
                                        Toast.makeText(view, "Password reset email sent to ${emailState.value}", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(view, "Please enter your email first.", Toast.LENGTH_SHORT).show()
                                    }
                                }) {
                                    Text(
                                        text = "Forgot?",
                                        style = MaterialTheme.typography.bodyLarge.copy(color = DarkGray),
                                        modifier = Modifier.padding(end = 8.dp) // Adjust as needed
                                    )
                                }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(modifier = Modifier.width(220.dp),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp),
                    onClick = {
                        signInAction(emailState.value, passwordState.value)
                    }) {
                    Text("LOGIN")
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(
                    modifier = Modifier.width(220.dp),
                    onClick = { Toast.makeText(view, "Login with Google", Toast.LENGTH_SHORT).show()},
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 15.dp)
                    ) {
                        Image(painter = painterResource(id = R.drawable.icons8_google),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp))
                        Text("Login with Google",
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                TextButton(
                    modifier = Modifier.width(220.dp),
                    onClick = { Toast.makeText(view, "Login with Facebook", Toast.LENGTH_SHORT).show() },
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(painter = painterResource(id = R.drawable.ic_facebook), contentDescription = "", modifier = Modifier.size(24.dp))
                        Text("Login with Facebook", modifier = Modifier.padding(start = 5.dp))
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
                    val signUpString = AnnotatedString.Builder().apply {
                        withStyle(
                            style = SpanStyle(
                                textDecoration = TextDecoration.Underline,
                                color = blue2
                            )
                        ) {
                            append(signUpText)
                        }
                    }.toAnnotatedString()

                    ClickableText(
                        text = signUpString,
                        style = MaterialTheme.typography.bodyLarge,
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
                            .align(Alignment.Center)
                            .padding(8.dp)
                            .padding(top = 40.dp)
                    )
                }
            }

        }
    }
}
