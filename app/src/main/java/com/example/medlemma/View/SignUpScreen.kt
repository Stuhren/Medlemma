package com.example.medlemma.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.medlemma.ViewModel.SignupViewModel
import com.example.medlemma.ui.theme.Blue

val customShape = Shapes(
    small = RoundedCornerShape(10.dp), // Adjust the radius as needed
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(40.dp)
)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController, viewModel: SignupViewModel, signUpAction: (email: String, pass: String, confirmPass: String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val errorMessage by viewModel.signupErrorMessage.observeAsState()

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
            Text(text = "Sign Up", fontSize = 34.sp, modifier = Modifier.align(Alignment.Start))
            Text(text = "Please register to continue.", fontSize = 16.sp, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(24.dp))

            val emailState = rememberSaveable { mutableStateOf("") }
            val passwordState = rememberSaveable { mutableStateOf("") }
            val confirmPassState = rememberSaveable { mutableStateOf("") }

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
                shape = customShape.large
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
                visualTransformation = PasswordVisualTransformation(),
                shape = customShape.large
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassState.value,
                onValueChange = { confirmPassState.value = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
                visualTransformation = PasswordVisualTransformation(),
                shape = customShape.large
            )
            Spacer(modifier = Modifier.height(26.dp))

            Button(
                modifier = Modifier.width(220.dp),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp),
                onClick = {
                signUpAction(emailState.value, passwordState.value, confirmPassState.value)
            }) {
                Text("Sign Up")
            }

            Spacer(modifier = Modifier.height(100.dp))


            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.width(220.dp),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.icons8_google), contentDescription = "", modifier = Modifier.size(24.dp))
                    Text("  Sign up with Google", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.width(220.dp),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.ic_facebook), contentDescription = "")
                    Text("  Sign up with Facebook", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account? ")

                val signInText = "Sign In"
                val signInString = AnnotatedString(signInText)

                ClickableText(
                    text = signInString,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Blue),
                    onClick = {
                        navController.navigate("signIn")
                    }
                )
            }

            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                        .padding(bottom = 220.dp) // Ensure this is above the "Sign In" text.
                )
            }
        }

    }
}

@Composable
fun SocialMediaButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text)
        }
    }
}
