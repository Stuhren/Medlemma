package com.example.medlemma.Model

import android.net.Uri
import androidx.compose.runtime.MutableState
import com.example.medlemma.ViewModel.Company
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun uploadImageToFirebaseStoragePersonalPhotos(email: String, uri: Uri, imageName: String, callback: (String?) -> Unit) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child("personal_photos/$imageName")

    val uploadTask = storageRef.putFile(uri)

    uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let {
                throw it
            }
        }
        storageRef.downloadUrl
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val downloadUri = task.result
            callback(downloadUri.toString())
            // Now, update the identificationURL with the download URL
            updateIdentificationURLForMember(email, downloadUri.toString()) { updateResult ->
                if (updateResult) {
                    // Successfully updated identificationURL
                } else {
                    // Failed to update identificationURL
                }
            }

        } else {
            callback(null) // Return null to indicate an error
        }
    }
}

fun updateIdentificationURLForMember(
    email: String,
    newIdentificationURL: String,
    callback: (Boolean) -> Unit
) {
    val database = FirebaseDatabase.getInstance()
    val membersRef = database.getReference("members")

    // Query the member with the specified email
    val query = membersRef.orderByChild("email").equalTo(email)

    query.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                // Update the identificationURL for the first matching member
                for (dataSnapshot in snapshot.children) {
                    val memberToUpdateRef = dataSnapshot.ref
                    memberToUpdateRef.child("identificationURL").setValue(newIdentificationURL)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                callback(true) // Successfully updated the member
                            } else {
                                callback(false) // Failed to update the member
                            }
                        }
                    break // Exit the loop after updating the first matching member
                }
            } else {
                callback(false) // Member with the specified email not found
            }
        }

        override fun onCancelled(error: DatabaseError) {
            callback(false) // Error occurred during the query
        }
    })
}

fun isAdminUser(email: String?, onIsAdmin: (Boolean) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val membersRef = database.getReference("members")

    // Query the database to find the member with the specified email
    val query = membersRef.orderByChild("email").equalTo(email)
    query.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                for (memberSnapshot in snapshot.children) {
                    val role = memberSnapshot.child("role").getValue(String::class.java)
                    if (role == "admin") {
                        // The user has an "admin" role
                        onIsAdmin(true)
                    } else {
                        // The user does not have an "admin" role
                        onIsAdmin(false)
                    }
                    // Exit the loop since we found the user
                    return
                }
            } else {
                // User not found, or no such email in the database
                onIsAdmin(false)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle the error here
            onIsAdmin(false)
        }
    })
}



