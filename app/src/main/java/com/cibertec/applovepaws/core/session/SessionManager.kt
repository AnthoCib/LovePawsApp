package com.cibertec.applovepaws.core.session
import android.content.Context

object SessionManager{
    private const val PREFS_NOMBRE = "lovepaws_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_USERNAME = "username"

    fun guardarSesion(context: Context, token: String, username: String) {
        val prefs = context.getSharedPreferences(PREFS_NOMBRE, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_TOKEN, token).putString(KEY_USERNAME, username).apply()
    }

    fun obtenerToken(context: Context): String? {
        return context.getSharedPreferences(PREFS_NOMBRE, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)
    }

    fun obtenerUsername(context: Context): String? {
        return context.getSharedPreferences(PREFS_NOMBRE, Context.MODE_PRIVATE)
            .getString(KEY_USERNAME, null)
    }

    fun cerrarSesion(context: Context) {
        context.getSharedPreferences(PREFS_NOMBRE, Context.MODE_PRIVATE).edit().clear().apply()
    }

    fun estaLogueado(context: Context): Boolean {
        return obtenerToken(context) != null
    }
}
