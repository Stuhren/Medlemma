package com.example.medlemma.ViewModel


import androidx.lifecycle.ViewModel
import com.example.medlemma.Model.CompanyModel
import com.example.medlemma.Model.ViewCompany
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CompanyViewModel(val companyModel: CompanyModel) : ViewModel() {
    val companyData = companyModel.companyData

    fun deleteCompany(company: ViewCompany) {
        val database = FirebaseDatabase.getInstance()
        val companiesRef = database.getReference("companies")
        val companyId = company.id

        companiesRef.orderByChild("id").equalTo(companyId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (companySnapshot in dataSnapshot.children) {
                    // companySnapshot.key will give you the unique identifier (Firebase Database key) of the company with the matching ID
                    val uniqueIdentifier = companySnapshot.key
                    if (uniqueIdentifier != null) {
                        val companyReference = companiesRef.child(uniqueIdentifier)
                        companyReference.removeValue()
                            .addOnSuccessListener {

                            }
                            .addOnFailureListener { error ->
                                // Handle the error, e.g., show a message or log the error
                            }
                        println("Unique Identifier: $uniqueIdentifier")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error if needed
                println("Firebase Database Error: ${databaseError.message}")
            }
        })
    }
}