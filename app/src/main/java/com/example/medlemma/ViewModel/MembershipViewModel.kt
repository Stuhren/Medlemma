package com.example.medlemma.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.medlemma.Model.FirebaseRepository
import kotlinx.coroutines.Dispatchers

class MyMembershipsViewModel : ViewModel() {

    fun fetchCategories() = liveData(Dispatchers.IO) {
        val data = FirebaseRepository.getCategories()
        emit(data)
    }
    fun fetchLogos() = liveData(Dispatchers.IO) {
        val data = FirebaseRepository.getCompanyLogo()
        emit(data)
    }

    fun fetchAllData() = liveData(Dispatchers.IO) {
        val data = FirebaseRepository.getAllItems()
        emit(data)
    }


}