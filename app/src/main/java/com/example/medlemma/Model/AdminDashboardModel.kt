package com.example.medlemma.Model

import com.google.firebase.database.*

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