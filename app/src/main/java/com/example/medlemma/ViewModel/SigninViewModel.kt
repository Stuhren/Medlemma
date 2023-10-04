package com.example.medlemma.ViewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private val auth: FirebaseAuth = Firebase.auth



class SigninViewModel : ViewModel() {

    fun signIn(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { userCredential ->
                // Signed in
                val user = userCredential.user
                println("SIGNEND IN")
                // ...
            }
            .addOnFailureListener { exception ->
                val errorCode = (exception as FirebaseAuthException).errorCode
                val errorMessage = exception.message
            }
    }
}