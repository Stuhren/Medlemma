package com.example.medlemma.Model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FirebaseRepository {
    private val database = FirebaseDatabase.getInstance().getReference()

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
}
