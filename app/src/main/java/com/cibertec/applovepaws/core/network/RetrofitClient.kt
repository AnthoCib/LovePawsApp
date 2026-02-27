package com.cibertec.applovepaws.core.network

import com.cibertec.applovepaws.feature_adopcion.data.api.AdoptionApi
import com.cibertec.applovepaws.feature_mascota.data.api.MascotaApi
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

object RetrofitClient {

    private class SessionCookieJar : CookieJar {
        private val store = mutableMapOf<String, MutableList<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            val host = url.host
            val current = store.getOrPut(host) { mutableListOf() }

            cookies.forEach { newCookie ->
                current.removeAll { old ->
                    old.name == newCookie.name && old.path == newCookie.path && old.domain == newCookie.domain
                }
                current.add(newCookie)
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> = store[url.host].orEmpty()

        fun clear() = store.clear()
    }

    interface AuthApi {
        @FormUrlEncoded
        @POST("login")
        suspend fun login(
            @Field("username") username: String,
            @Field("password") password: String
        ): Response<ResponseBody>
    }

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val cookieJar = SessionCookieJar()

    fun clearSession() {
        cookieJar.clear()
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .retryOnConnectionFailure(true)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val req = chain.request().newBuilder()
                .header("Accept", "application/json")
                .build()
            chain.proceed(req)
        }
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val mascotaApi: MascotaApi = retrofit.create(MascotaApi::class.java)
    val adoptionApi: AdoptionApi = retrofit.create(AdoptionApi::class.java)
}