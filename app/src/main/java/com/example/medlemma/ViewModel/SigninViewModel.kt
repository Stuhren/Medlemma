package com.example.medlemma.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private val auth: FirebaseAuth = Firebase.auth

class SigninViewModel : ViewModel() {

    // Live stores the error message
    val errorMessage = MutableLiveData<String?>()

    fun signIn(email: String, pass: String) {
        if(email.isEmpty() || pass.isEmpty()) {
            errorMessage.value = "All fields are required."
            return
        }
        // Attempt sign in
        auth.signInWithEmailAndPassword(email, pass)
            // Sign in successful
            .addOnSuccessListener { userCredential ->
                errorMessage.value = null // clear any previous errors
                // Signed in
                val user = userCredential.user
                println("SIGNEND IN")
                // ...
            }
            // Sign in failed
            .addOnFailureListener { exception ->
                // Converts the error message to a user friendly message and sets the errorMessage.value to this
                when (exception) {
                    is FirebaseAuthException -> {
                        println("SignInError: Error Code -> ${exception.errorCode}")
                        errorMessage.value = when (exception.errorCode) {
                            "ERROR_INVALID_EMAIL" -> "Please enter a valid email address."
                            // ... other FirebaseAuthException error codes
                            else -> "An unexpected error occurred. Please try again."
                        }
                    }
                    is FirebaseException -> {
                        println("SignInError: Message -> ${exception.message}")
                        errorMessage.value = when (exception.message) {
                            "An internal error has occurred. [ INVALID_LOGIN_CREDENTIALS ]" -> "Incorrect password. Please try again."
                            // ... other FirebaseException error messages
                            else -> "An unexpected error occurred. Please try again."
                        }
                    }
                    else -> {
                        println("SignInError: Exception of unknown type -> ${exception::class.java}")
                        errorMessage.value = "An unexpected error occurred. Please try again."
                    }
                }
            }
    }
}