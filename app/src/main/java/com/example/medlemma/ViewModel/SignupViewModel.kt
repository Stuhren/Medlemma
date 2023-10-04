package com.example.medlemma.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

val auth: FirebaseAuth = Firebase.auth



class SignupViewModel : ViewModel() {

    // Example function for signing up (replace with your logic)
    fun signUp(email: String, pass: String, confirmPass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Signed up
                    val user = task.result?.user
                    // ...
                } else {
                    // If sign up fails, display a message to the user.
                    val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
                    val errorMessage = task.exception?.message
                    // ...
                }
            }
    }
}