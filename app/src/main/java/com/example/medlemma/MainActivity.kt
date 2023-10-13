package com.example.medlemma

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medlemma.ui.theme.MedlemmaTheme
import com.example.medlemma.View.SignUpScreen
import com.example.medlemma.View.SignInScreen
import com.example.medlemma.View.AdminDashboard
import com.example.medlemma.View.BrowseMemberships
import com.example.medlemma.View.DrawerBody
import com.example.medlemma.View.DrawerHeader
import com.example.medlemma.View.MyMemberships
import com.example.medlemma.ViewModel.AppBar
import com.example.medlemma.ViewModel.MenuItem
import com.example.medlemma.ViewModel.MyMembershipsViewModel
import com.example.medlemma.ViewModel.SigninViewModel
import com.example.medlemma.ViewModel.SignupViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private lateinit var signUpViewModel: SignupViewModel
    private lateinit var signInViewModel: SigninViewModel
    private lateinit var myMembershipsViewModel: MyMembershipsViewModel

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signUpViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        signInViewModel = ViewModelProvider(this).get(SigninViewModel::class.java)
        myMembershipsViewModel = ViewModelProvider(this).get(MyMembershipsViewModel::class.java)



        var currentEmail: String = ""

        setContent {
            val navController = rememberNavController()

            MedlemmaTheme {
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()

                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        AppBar(
                            onNavigationIconClick = {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            }
                        )
                    },
                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                    drawerContent = {
                        // Use NavHostController to check the current route
                        DrawerHeader()
                        DrawerBody(
                            items = listOf(
                                MenuItem(
                                    id = "myMemberships?email={email}",
                                    title = "My Memberships",
                                    contentDescription = "My Memberships",
                                    icon = Icons.Default.CheckCircle
                                ),
                                MenuItem(
                                    id = "browseMemberships",
                                    title = "Browse Memberships",
                                    contentDescription = "Browse Memberships",
                                    icon = Icons.Default.Search
                                ),
                                MenuItem(
                                    id = "adminDashboard",
                                    title = "Admin Dashboard",
                                    contentDescription = "Admin Dashboard",
                                    icon = Icons.Default.Edit
                                ),
                                MenuItem(
                                    id = "signOut",
                                    title = "Sign Out",
                                    contentDescription = "Sign Out",
                                    icon = Icons.Default.ExitToApp
                                )
                            ), onItemClick = { items ->
                                //navController.navigate(it.id)
                                if (items.id == "myMemberships?email={email}") { // Replace with the user's email
                                    val route = "myMemberships?email=$currentEmail"
                                    navController.navigate(route)
                                } else {
                                    navController.navigate(items.id)
                                }
                            },
                            scaffoldState = scaffoldState
                        )
                    }
                ) {
                    NavHost(navController, startDestination = "signIn") {
                        composable("signIn") {
                            val currentEmailLogin = remember { mutableStateOf("") }
                            SignInScreen(navController, signInViewModel) { email, pass ->
                                signInViewModel.signIn(navController, email, pass)
                                currentEmailLogin.value = email
                            }
                            signInViewModel.errorMessage.observe(this@MainActivity) { error ->
                                if (error == null) { // Check if login was successful based on no error message
                                    currentEmail = currentEmailLogin.value
                                    val route = "myMemberships?email=$currentEmail"
                                    navController.navigate(route)

                                }
                            }
                        }
                        composable("signUp") {
                            SignUpScreen(
                                navController,
                                signUpViewModel
                            ) { email, pass, confirmPass ->
                                signUpViewModel.signUp(navController, email, pass, confirmPass)
                            }
                        }
                        composable("adminDashboard") {
                            AdminDashboard()
                        }
                        composable("browseMemberships") {
                            BrowseMemberships()
                        }
                        composable("myMemberships?email={email}") { backStackEntry ->
                            // Retrieve the email from the navigation arguments
                            val email = backStackEntry.arguments?.getString("email")

                            MyMemberships(email)

                        }
                        composable("signOut") {
                            signInViewModel.signOut()
                            SignInScreen(navController, signInViewModel) { email, pass ->
                                signInViewModel.signIn(navController, email, pass)
                            }
                        }
                    }
                }
            }
        }
    }
}