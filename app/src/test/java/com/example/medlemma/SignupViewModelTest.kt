package com.example.medlemma

import org.junit.Test
import org.junit.Before


class SignupViewModelTest {

    lateinit var signupViewModel: FakeSignupViewModel

    @Before
    fun setUp() {
        signupViewModel = FakeSignupViewModel()
    }

    @Test
    fun testSignUpWithEmptyInput() {
        val email = ""
        val password = ""
        val confirmPassword = ""

        signupViewModel.signUp(email, password, confirmPassword)

        val errorMessage = signupViewModel.signupErrorMessage
        assert(errorMessage == "All fields are required.")
    }

    @Test
    fun testSignUpWithCorrectInput() {
        val email = "user@example.com"
        val password = "Password123"
        val confirmPassword = "Password123"

        signupViewModel.signUp(email, password, confirmPassword)

        val errorMessage = signupViewModel.signupErrorMessage
        assert(errorMessage == null)
    }
}

class FakeSignupViewModel {
    var signupErrorMessage: String? = null

    fun signUp(email: String, pass: String, confirmPass: String) {
        if (email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            signupErrorMessage = "All fields are required."
            return
        }

        signupErrorMessage = null
    }
}