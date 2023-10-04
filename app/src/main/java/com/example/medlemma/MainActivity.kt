package com.example.medlemma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medlemma.ui.theme.MedlemmaTheme

import com.example.medlemma.View.SignUpScreen
import com.example.medlemma.View.SignInScreen
import com.example.medlemma.ViewModel.SignupViewModel
import com.example.medlemma.ViewModel.SigninViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    private lateinit var signUpViewModel: SignupViewModel
    private lateinit var signInViewModel: SigninViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signUpViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        signInViewModel = ViewModelProvider(this).get(SigninViewModel::class.java)

        setContent {
            MedlemmaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "signUp") {
                        composable("signUp") {
                            SignUpScreen(navController) { email, pass, confirmPass ->
                                signUpViewModel.signUp(email, pass, confirmPass)
                            }
                        }
                        composable("signIn") {
                            SignInScreen(navController) { email, pass ->
                                signInViewModel.signIn(email,pass)
                            }
                        }
                        // Add other composables/screens as needed.
                    }
                }
            }
        }
    }
}