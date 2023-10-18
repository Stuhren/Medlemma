package com.example.medlemma.Model

import com.google.firebase.database.*
import android.net.Uri
import com.example.medlemma.ViewModel.Company
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

data class ViewCompany(
    val companyName: String,
    val id: String,
    val category: String,
    val registerUrl: String,
    val companyLogo: String
)

fun generateCustomID(callback: (String) -> Unit) {
    // Get the current timestamp in milliseconds
    val timestamp = System.currentTimeMillis()

    // Get the current date in a specific format
    val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
    val currentDate = dateFormat.format(Date())

    // Generate a random UUID
    val randomUUID = UUID.randomUUID().toString()

    // Combine the timestamp, current date, and random UUID to create a custom ID
    val customID = "$currentDate-$timestamp-$randomUUID"

    callback(customID)
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

fun updateCompanyInFirebase(
    customId: String, // Pass the custom ID as a parameter
    updatedCategory: String,
    updatedCompanyName: String,
    updatedRegisterUrl: String,
    updatedCompanyLogo: String,
    callback: (Boolean) -> Unit
) {
    val database = FirebaseDatabase.getInstance()
    val companiesRef = database.getReference("companies")

    // Assuming you have a structure where "id" is a field within the company data
    // You can query the company with your custom ID
    val query = companiesRef.orderByChild("id").equalTo(customId)

    query.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                // Update the first matching company (custom ID)
                for (dataSnapshot in snapshot.children) {
                    val companyToUpdateRef = dataSnapshot.ref
                    val updatedCompany = Company(updatedCategory, updatedCompanyName, updatedRegisterUrl, customId, updatedCompanyLogo)
                    companyToUpdateRef.setValue(updatedCompany)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                callback(true) // Successfully updated the company
                            } else {
                                callback(false) // Failed to update the company
                            }
                        }
                    break // Exit the loop after updating the first matching company
                }
            } else {
                callback(false) // Custom ID not found
            }
        }

        override fun onCancelled(error: DatabaseError) {
            callback(false) // Error occurred during the query
        }
    })
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