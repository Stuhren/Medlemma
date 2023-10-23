package com.example.medlemma.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.medlemma.Model.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyMembershipsViewModel : ViewModel() {


    fun fetchAllMembers() = liveData(Dispatchers.IO) {
        val data = FirebaseRepository.getAllMembers()
        emit(data)
    }

    fun fetchAllCompanies() = liveData(Dispatchers.IO) {
        val data = FirebaseRepository.getAllCompanies()
        emit(data)
    }
    fun deleteMembership(email: String, companyId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                FirebaseRepository.deleteCompanyFromMember(email, companyId)
                withContext(Dispatchers.Main) {
                    // Handle UI updates or success actions
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Handle error in the UI
                }
            }
        }
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