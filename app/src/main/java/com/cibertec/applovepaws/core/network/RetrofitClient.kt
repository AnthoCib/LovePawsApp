package com.cibertec.applovepaws.core.network

import com.cibertec.applovepaws.feature_adopcion.data.api.AdoptionApi
import com.cibertec.applovepaws.feature_login.api.LoginApi
import com.cibertec.applovepaws.feature_mascota.data.api.MascotaApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/" // Cambia por tu host

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()



    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val adoptionApi: AdoptionApi by lazy {
        retrofit.create(AdoptionApi::class.java)
    }

    val mascotaApi: MascotaApi by lazy {
        retrofit.create(MascotaApi::class.java)
    }

    val loginApi: LoginApi by lazy {
        retrofit.create(LoginApi::class.java)
    }
}