package com.example.medlemma.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.medlemma.Model.generateCustomID
import com.example.medlemma.View.IconState
import kotlinx.coroutines.launch

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

private val auth: FirebaseAuth = Firebase.auth



class SignupViewModel : ViewModel() {
    // LiveData for holding the error message
    val signupErrorMessage = MutableLiveData<String?>()

    fun signUp(navController: NavController, email: String, pass: String, confirmPass: String) {
        val membershipList = listOf("yy","xx")
        var newUid = ""
        if(email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            signupErrorMessage.value = "All fields are required."
            return
        }
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signupErrorMessage.value = null // clear any previous errors
                    val user = task.result?.user
                    generateCustomID{ customID ->
                        if (customID != null) {
                            newUid = customID
                        } else {
                            println("Failed to retrieve unique ID")
                        }
                    }

                    val member = Member(
                        currentMemberships = membershipList,
                        email = email,
                        identificationURL = "",
                        uid = newUid
                    )

                    val database = FirebaseDatabase.getInstance()
                    val membersRef = database.getReference("members")
                    membersRef.child(newUid).setValue(member)


                    navController.navigate("myMemberships")
                } else {
                    val exception = task.exception
                    when (exception) {
                        is FirebaseAuthException -> {
                            println("SignUpError: Error Code -> ${exception.errorCode}")
                            signupErrorMessage.value = when (exception.errorCode) {
                                "ERROR_EMAIL_ALREADY_IN_USE" -> "Email already in use. Please try another."
                                "ERROR_INVALID_EMAIL" -> "Please enter a valid email address."
                                "ERROR_WEAK_PASSWORD" -> "Password is too weak. Please use a stronger password."
                                // ... other FirebaseAuthException error codes
                                else -> "An unexpected error occurred. Please try again."
                            }
                        }
                        else -> {
                            println("SignUpError: Exception of unknown type -> ${exception?.javaClass}")
                            signupErrorMessage.value = "An unexpected error occurred. Please try again."
                        }
                    }
                }
            }
    }
}
