package com.cibertec.applovepaws.core.network

import com.cibertec.applovepaws.BuildConfig
import com.cibertec.applovepaws.feature_adopcion.data.api.AdoptionApi
import com.cibertec.applovepaws.feature_login.data.api.AuthApi
import com.cibertec.applovepaws.feature_mascota.data.api.MascotaApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val baseUrl: String = BuildConfig.API_BASE_URL

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val adoptionApi: AdoptionApi by lazy {
        retrofit.create(AdoptionApi::class.java)
    }

    val mascotaApi: MascotaApi by lazy {
        retrofit.create(MascotaApi::class.java)
    }
}
