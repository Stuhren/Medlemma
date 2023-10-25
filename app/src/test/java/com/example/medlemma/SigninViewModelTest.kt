package com.example.medlemma

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class SigninViewModelTest {

    lateinit var signinViewModel: FakeSigninViewModel

    @Before
    fun setUp() {
        signinViewModel = FakeSigninViewModel()
    }

    @Test
    fun testSignInWithValidInput() {
        val email = "valid@email.com"
        val password = "validpassword"

        signinViewModel.signIn(email, password)

        val errorMessage = signinViewModel.errorMessage
        assertNull(errorMessage)
    }

    @Test
    fun testSignInWithInvalidEmail() {
        val email = "invalid-email"
        val password = "validpassword"

        signinViewModel.signIn(email, password)

        val errorMessage = signinViewModel.errorMessage
        assertEquals("Please enter a valid email address.", errorMessage)
    }

    @Test
    fun testSignInWithIncorrectPassword() {
        val email = "valid@email.com"
        val password = "incorrectpassword"

        signinViewModel.signIn(email, password)

        val errorMessage = signinViewModel.errorMessage
        assertEquals("Incorrect password. Please try again.", errorMessage)
    }

}

class FakeSigninViewModel {
    var errorMessage: String? = null

    fun signIn(email: String, pass: String) {
        if (email.isEmpty() || pass.isEmpty()) {
            errorMessage = "All fields are required."
            return
        }

        if (email == "valid@email.com" && pass == "validpassword") {
            errorMessage = null
        } else {
            when {
                email != "valid@email.com" -> errorMessage = "Please enter a valid email address."
                pass != "validpassword" -> errorMessage = "Incorrect password. Please try again."
                else -> errorMessage = "An unexpected error occurred. Please try again."
            }
        }
    }
}
