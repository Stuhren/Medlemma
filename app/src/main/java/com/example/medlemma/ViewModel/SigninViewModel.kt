

package com.example.medlemma.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

private val auth: FirebaseAuth = Firebase.auth

class SigninViewModel : ViewModel() {
    // Live stores the error message
    val errorMessage = MutableLiveData<String?>()

    fun signIn(
        navController: NavController,
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFailure: (MutableLiveData<String?>) -> Unit
    ) {
        if (email.isEmpty() || pass.isEmpty()) {
            onFailure(errorMessage)
            return
        }

        // Attempt sign in
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { userCredential ->
                errorMessage.value = null // clear any previous errors
                // Navigate to myMemberships after successful login
                navController.navigate("myMemberships") {
                    // Clears the navigation stack to prevent going back to signIn after logging in
                    popUpTo("signIn") { inclusive = true }
                }
                onSuccess()
            }
            // Sign in failed
            .addOnFailureListener { exception ->
                // Converts the error message to a user-friendly message and passes it to the failure callback
                when (exception) {
                    is FirebaseAuthException -> {
                        println("SignInError: Error Code -> ${exception.errorCode}")
                        errorMessage.value = when (exception.errorCode) {
                            "ERROR_INVALID_EMAIL" -> "Please enter a valid email address."
                            // ... other FirebaseAuthException error codes
                            else -> "An unexpected error occurred. Please try again."
                        }
                        onFailure(errorMessage)
                    }
                    is FirebaseException -> {
                        println("SignInError: Message -> ${exception.message}")
                        errorMessage.value = when (exception.message) {
                            "An internal error has occurred. [ INVALID_LOGIN_CREDENTIALS ]" -> "Incorrect password. Please try again."
                            // ... other FirebaseException error messages
                            else -> "An unexpected error occurred. Please try again."
                        }
                        onFailure(errorMessage)
                    }
                    else -> {
                        println("SignInError: Exception of unknown type -> ${exception::class.java}")
                        errorMessage.value = "An unexpected error occurred. Please try again."
                        onFailure(errorMessage)
                    }
                }
            }
    }

    fun signOut(){
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
    }

    fun resetPassword(email:String){
        val auth = Firebase.auth
        println("RESET")

        try {
            auth.sendPasswordResetEmail(email)
            // Password reset email sent!
            // ..
        } catch (e: Exception) {
            val errorCode = e.localizedMessage
            val errorMessage = e.message
            // ..
        }
    }
}