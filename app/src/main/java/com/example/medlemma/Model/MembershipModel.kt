package com.example.medlemma.Model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FirebaseRepository {
    private val database = FirebaseDatabase.getInstance().getReference()

    data class Company(
        val id: String,
        val companyName: String,
        val companyLogo: String,
        val registerURL: String,
        val category: String
    )

    data class Member(
        val uid: String,
        val email: String,
        val currentMemberships: List<String>,
        val identificationURL: String,
        val role: String
    )

    // function to get a item with a specific id from the database
    suspend fun getAllCompanies(): List<Company> {
        return suspendCancellableCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val allCompanies = mutableListOf<Company>()
                    dataSnapshot.children.forEach { companySnapshot ->
                        val id = companySnapshot.child("id").value.toString()
                        val companyName = companySnapshot.child("companyName").value.toString()
                        val companyLogo = companySnapshot.child("companyLogo").value.toString()
                        val registerURL = companySnapshot.child("registerURL").value.toString()
                        val category = companySnapshot.child("category").value.toString()
                        val company = Company(id, companyName, companyLogo, registerURL, category)
                        allCompanies.add(company)
                    }
                    continuation.resume(allCompanies)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    continuation.resumeWithException(databaseError.toException())
                }
            }
            database.child("companies").addListenerForSingleValueEvent(listener)
            continuation.invokeOnCancellation { database.removeEventListener(listener) }
        }
    }

    suspend fun getAllMembers(): List<Member> {
        return try {
            val dataSnapshot = database.child("members").get().await()
            val allMembers = mutableListOf<Member>()

            for (memberSnapshot in dataSnapshot.children) {
                val uid = memberSnapshot.child("uid").getValue(String::class.java) ?: ""
                val email = memberSnapshot.child("email").getValue(String::class.java) ?: ""
                val currentMemberships = memberSnapshot.child("currentMemberships").getValue(object : GenericTypeIndicator<List<String>>() {}) ?: emptyList()
                val identificationURL = memberSnapshot.child("identificationURL").getValue(String::class.java) ?: ""
                val role = memberSnapshot.child("role").getValue(String::class.java) ?: ""

                val member = Member(uid, email, currentMemberships, identificationURL, role)
                allMembers.add(member)
            }

            allMembers
        } catch (e: Exception) {
            // Handle exceptions or return an empty list if needed
            emptyList()
        }
    }


    /*
        suspend fun getCategories(): List<String> {
            return suspendCancellableCoroutine { continuation ->
                val listener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val categories = mutableListOf<String>()
                        dataSnapshot.children.forEach {
                            val category = it.child("category").value.toString()
                            categories.add(category)
                        }
                        continuation.resume(categories)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        continuation.resumeWithException(databaseError.toException())
                    }
                }
                database.addListenerForSingleValueEvent(listener)
                continuation.invokeOnCancellation { database.removeEventListener(listener) }
            }
        }

        suspend fun getCompanyLogo(): List<String> {
            return suspendCancellableCoroutine { continuation ->
                val listener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val companyLogos = mutableListOf<String>()
                        dataSnapshot.children.forEach {
                            val companyLogo = it.child("companyLogo").value.toString()
                            companyLogos.add(companyLogo)
                        }
                        continuation.resume(companyLogos)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        continuation.resumeWithException(databaseError.toException())
                    }
                }
                database.addListenerForSingleValueEvent(listener)
                continuation.invokeOnCancellation { database.removeEventListener(listener) }
            }
        }

         */
}
