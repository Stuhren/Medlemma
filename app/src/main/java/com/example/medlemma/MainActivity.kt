package com.example.medlemma

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.medlemma.Model.CompanyModel
import com.example.medlemma.Model.isAdminUser
import com.example.medlemma.ui.theme.MedlemmaTheme
import com.example.medlemma.View.SignUpScreen
import com.example.medlemma.View.SignInScreen
import com.example.medlemma.View.AdminDashboard
import com.example.medlemma.View.BrowseMemberships
import com.example.medlemma.View.DrawerBody
import com.example.medlemma.View.DrawerHeader
import com.example.medlemma.View.MyMemberships
import com.example.medlemma.View.PersonalInfo
import com.example.medlemma.ViewModel.AppBar
import com.example.medlemma.ViewModel.CompanyViewModel
import com.example.medlemma.ViewModel.MenuItem
import com.example.medlemma.ViewModel.MyMembershipsViewModel
import com.example.medlemma.ViewModel.SigninViewModel
import com.example.medlemma.ViewModel.SignupViewModel
import com.example.medlemma.ViewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    init {
        instance = this
    }

    companion object {
        private var instance: MainActivity? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

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
        val userViewModel: UserViewModel by viewModels()

        setContent {
            val navController = rememberNavController()
            val isAdminUser = remember { mutableStateOf(false) }

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
                            isAdminUser(userViewModel.userEmail.value) { isAdmin ->
                                isAdminUser.value = isAdmin
                            }
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
                            items = if (isAdminUser.value) {
                                // If the user is an admin, show all menu items, including Admin Dashboard
                                listOf(
                                    MenuItem(
                                        id = "myMemberships",
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
                                        id = "personalInfo",
                                        title = "Personal Info",
                                        contentDescription = "Edit personal info",
                                        icon = Icons.Default.AccountBox
                                    ),
                                    MenuItem(
                                        id = "signOut",
                                        title = "Sign Out",
                                        contentDescription = "Sign Out",
                                        icon = Icons.Default.ExitToApp
                                    )
                                )
                            } else {
                                // If the user is not an admin, show all menu items except Admin Dashboard
                                listOf(
                                    MenuItem(
                                        id = "myMemberships",
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
                                        id = "personalInfo",
                                        title = "Personal Info",
                                        contentDescription = "Edit personal info",
                                        icon = Icons.Default.AccountBox
                                    ),
                                    MenuItem(
                                        id = "signOut",
                                        title = "Sign Out",
                                        contentDescription = "Sign Out",
                                        icon = Icons.Default.ExitToApp
                                    )
                                )
                            },
                            onItemClick = { items ->

                                when (items.id) {
                                    "signOut" -> {
                                        userViewModel.saveUserEmail(null)
                                        signInViewModel.signOut()
                                        navController.navigate("signIn")
                                    }
                                    else -> navController.navigate(items.id)
                                }

                            },
                            scaffoldState = scaffoldState
                        )
                    }
                ) {
                    NavHost(navController, startDestination = "signIn") {
                        composable("signIn") {
                            SignInScreen(navController, signInViewModel) { email, pass ->
                                signInViewModel.signIn(navController, email, pass)
                                userViewModel.saveUserEmail(email)
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
                        composable("myMemberships") {
                            protectRoute(navController) { user ->
                                MyMemberships(user)
                            }
                        }
                        composable("signOut") {
                            userViewModel.saveUserEmail(null)
                            signInViewModel.signOut()
                            navController.navigate("signIn")
                        }
                        composable("personalInfo") {
                            PersonalInfo(userViewModel.userEmail.value)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun protectRoute(navController: NavController, content: @Composable (user: String) -> Unit) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        content(currentUser.email ?: "")  // passing email as the user. Adjust accordingly if you have a different requirement.
    } else {
        // If user isn't authenticated, navigate them back to the sign-in screen
        navController.navigate("signIn")
    }
}