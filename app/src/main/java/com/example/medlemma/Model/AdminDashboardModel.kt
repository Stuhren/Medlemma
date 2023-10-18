package com.example.medlemma.Model

import com.google.firebase.database.*
import android.net.Uri
import com.example.medlemma.ViewModel.Company
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ViewCompany(
    val companyName: String,
    val id: String,
    val category: String,
    val registerUrl: String,
    val companyLogo: String
)

fun getCompanyCountFromFirebase(callback: (Int) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val companiesRef = database.getReference("companies")

    companiesRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val companyCount = dataSnapshot.childrenCount.toInt()
            callback(companyCount)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("Firebase Database Error: ${databaseError.message}")
            callback(-1) // Return -1 to indicate an error
        }
    })
}

fun uploadImageToFirebaseStorage(uri: Uri, imageName: String, callback: (String?) -> Unit) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child("Company_photos/$imageName")

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
        } else {
            callback(null) // Return null to indicate an error
        }
    }
}

fun addCompanyToFirebase(
    category: String,
    companyName: String,
    registerUrl: String,
    id: String,
    companyLogo: String,
    callback: (Boolean) -> Unit
) {
    val newCompany = Company(category, companyName, registerUrl, id, companyLogo)
    val database = FirebaseDatabase.getInstance()
    val companiesRef = database.getReference("companies")
    val newCompanyRef = companiesRef.push()

    newCompanyRef.setValue(newCompany)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true) // Successfully added the company
            } else {
                callback(false) // Failed to add the company
            }
        }
}

class CompanyModel {
    private val _companyData = MutableStateFlow(emptyList<ViewCompany>())
    val companyData = _companyData.asStateFlow()

    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("companies")

    private val listener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val companies = mutableListOf<ViewCompany>()
            for (childSnapshot in dataSnapshot.children) {
                val name = childSnapshot.child("companyName").getValue(String::class.java) ?: ""
                val id = childSnapshot.child("id").getValue(String::class.java) ?: ""
                val category = childSnapshot.child("category").getValue(String::class.java) ?: ""
                val registerUrl = childSnapshot.child("registerUrl").getValue(String::class.java) ?: ""
                val companyLogo = childSnapshot.child("companyLogo").getValue(String::class.java) ?: ""
                companies.add(ViewCompany(name, id, category, registerUrl, companyLogo))
            }
            _companyData.value = companies
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle database error if needed
        }
    }

    fun startListening() {
        reference.addValueEventListener(listener)
    }

    fun stopListening() {
        reference.removeEventListener(listener)
    }
}