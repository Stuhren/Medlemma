package com.example.medlemma.ViewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.medlemma.MainActivity

class UserViewModel: ViewModel() {
    private val sharedPreferencesName = "user_prefs"
    private val emailKey = "user_email"

    private val sharedPreferences: SharedPreferences =
        MainActivity.applicationContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    var userEmail: MutableState<String?> = mutableStateOf(null)

    init {
        // Load the user's email from SharedPreferences when the ViewModel is created
        userEmail.value = sharedPreferences.getString(emailKey, null)
    }

    fun saveUserEmail(email: String?) {
        userEmail.value = email

        // Save the user's email to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString(emailKey, email)
        editor.apply()
    }

}