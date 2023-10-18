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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.medlemma.Model.CompanyModel
import com.example.medlemma.ui.theme.MedlemmaTheme
import com.example.medlemma.View.SignUpScreen
import com.example.medlemma.View.SignInScreen
import com.example.medlemma.View.AdminDashboard
import com.example.medlemma.View.BrowseMemberships
import com.example.medlemma.View.DrawerBody
import com.example.medlemma.View.DrawerHeader
import com.example.medlemma.View.MyMemberships
import com.example.medlemma.ViewModel.AppBar
import com.example.medlemma.ViewModel.CompanyViewModel
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
    private val companyModel = CompanyModel()
    private val companyViewModel = CompanyViewModel(companyModel)

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

                // Observe the current backstack and get the current route
                val currentBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry.value?.destination?.route

                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        // Display AppBar only if the route is not signIn or signUp
                        if (currentRoute != "signIn" && currentRoute != "signUp") {
                            AppBar(
                                onNavigationIconClick = {
                                    scope.launch {
                                        scaffoldState.drawerState.open()
                                    }
                                }
                            )
                        }
                    },
                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                    drawerContent = {
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
                                if (items.id == "myMemberships?email={email}") {
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
                            AdminDashboard(companyViewModel = companyViewModel)
                        }
                        composable("browseMemberships") {
                            BrowseMemberships()
                        }
                        composable("myMemberships?email={email}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email")
                            MyMemberships(email)
                        }
                        composable("signOut") {
                            signInViewModel.signOut()
                            navController.navigate("signIn")
                        }
                    }
                }
            }
        }
    }
}


