package com.example.medlemma.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.medlemma.Model.FirebaseRepository
import kotlinx.coroutines.Dispatchers

class MyMembershipsViewModel : ViewModel() {


    fun fetchAllMembers() = liveData(Dispatchers.IO) {
        val data = FirebaseRepository.getAllMembers()
        emit(data)
    }

    fun fetchAllCompanies() = liveData(Dispatchers.IO) {
        val data = FirebaseRepository.getAllCompanies()
        emit(data)
    }

    fun sendCurrentUser(email: String) = liveData(Dispatchers.IO) {
        val emailSet = setOf(email)
        emit(emailSet)
    }

    /*
    fun fetchCategories() = liveData(Dispatchers.IO) {
        val data = FirebaseRepository.getCategories()
        emit(data)
    }
    fun fetchLogos() = liveData(Dispatchers.IO) {
        val data = FirebaseRepository.getCompanyLogo()
        emit(data)
    }

     */

}