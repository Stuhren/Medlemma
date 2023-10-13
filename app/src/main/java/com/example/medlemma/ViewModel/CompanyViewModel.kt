package com.example.medlemma.ViewModel

import androidx.lifecycle.ViewModel
import com.example.medlemma.Model.CompanyModel
import com.example.medlemma.Model.ViewCompany

class CompanyViewModel(val companyModel: CompanyModel) : ViewModel() {
    val companyData = companyModel.companyData

    fun deleteCompany(company: ViewCompany) {
        // Implement the delete logic here
    }
}