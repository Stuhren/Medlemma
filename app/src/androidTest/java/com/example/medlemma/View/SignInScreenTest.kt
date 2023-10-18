package com.example.medlemma.View

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medlemma.ViewModel.SigninViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            SignInScreen(navController = rememberNavController(), viewModel = SigninViewModel()) { _, _ -> }
        }
    }

    @Test
    fun testClickingForgotPassword() {
        composeTestRule.onNodeWithText("Forgot?").performClick()
    }

    @Test
    fun testClickingLoginButton() {
        composeTestRule.onNodeWithText("LOGIN").performClick()
    }

    @Test
    fun testClickingLoginWithGoogle() {
        composeTestRule.onNodeWithContentDescription("LoginGoogle").performClick()
    }

    @Test
    fun testClickingLoginWithFacebook() {
        composeTestRule.onNodeWithContentDescription("LoginFacebook").performClick()
    }

}