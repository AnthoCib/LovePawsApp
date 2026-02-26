package com.cibertec.applovepaws.feature_login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.core.utils.TokenManager
import com.cibertec.applovepaws.feature_login.data.repository.AuthRepository

class LoginViewModelFactory(
    context: Context
) : ViewModelProvider.Factory {

    private val repository = AuthRepository(
        api = RetrofitClient.authApi,
        tokenManager = TokenManager(context)
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
