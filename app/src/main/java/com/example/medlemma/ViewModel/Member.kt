package com.example.medlemma.ViewModel

data class Member(
    val currentMemberships: List<String>,
    val email: String,
    val identificationURL: String,
    val role: String = "user",
    val uid: String
)
