package com.example.medlemma.Model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FirebaseRepository {
    private val database = FirebaseDatabase.getInstance().getReference()

    data class Item(
        val companyName: String,
        val category: String,
        val companyLogo: String,
        val id: String
        // Add other properties of the item as needed
    )

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

    // function to get a item with a specific id from the database
    suspend fun getItemsWithSpecificId(specificId: String): List<Item> {
        return suspendCancellableCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val matchingItems = mutableListOf<Item>()
                    dataSnapshot.children.forEach { itemSnapshot ->
                        val itemId = itemSnapshot.child("id").value.toString() // Replace "idField" with the actual field containing the ID
                        if (itemId == specificId) {
                            val itemName = itemSnapshot.child("companyName").value.toString()
                            val itemCategory = itemSnapshot.child("category").value.toString()
                            val itemCompanyLogo = itemSnapshot.child("companyLogo").value.toString()
                            val itemId = itemSnapshot.child("id").value.toString()
                            // Add other properties of the item as needed
                            val item = Item(itemName, itemCategory, itemCompanyLogo, itemId)
                            matchingItems.add(item)
                        }
                    }
                    continuation.resume(matchingItems)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    continuation.resumeWithException(databaseError.toException())
                }
            }
            database.addListenerForSingleValueEvent(listener)
            continuation.invokeOnCancellation { database.removeEventListener(listener) }
        }
    }

    // function to get all the items from the datbase
    suspend fun getAllItems(): List<Item> {
        return suspendCancellableCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val allItems = mutableListOf<Item>()
                    dataSnapshot.children.forEach { itemSnapshot ->
                        val itemName = itemSnapshot.child("companyName").value.toString()
                        val itemCategory = itemSnapshot.child("category").value.toString()
                        val itemCompanyLogo = itemSnapshot.child("companyLogo").value.toString()
                        val itemId = itemSnapshot.child("id").value.toString()
                        val item = Item(itemName, itemCategory, itemCompanyLogo, itemId)
                        allItems.add(item)
                    }
                    continuation.resume(allItems)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    continuation.resumeWithException(databaseError.toException())
                }
            }
            database.addListenerForSingleValueEvent(listener)
            continuation.invokeOnCancellation { database.removeEventListener(listener) }
        }
    }


}
