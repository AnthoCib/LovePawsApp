package com.cibertec.applovepaws.feature_adopcion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_adopcion.data.repository.AdopcionRepository

class SolicitudViewModelFactory : ViewModelProvider.Factory {

    private val repository = AdopcionRepository(RetrofitClient.adoptionApi)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SolicitudViewModel::class.java)) {
            return SolicitudViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
