package com.cibertec.applovepaws.core.session

import android.content.Context

object SessionManager {
    private const val PREFS_NOMBRE = "lovepaws_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_USERNAME = "username"
    private const val KEY_ROLE = "role"

    fun guardarSesion(context: Context, token: String, username: String, role: String) {
        val prefs = context.getSharedPreferences(PREFS_NOMBRE, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USERNAME, username)
            .putString(KEY_ROLE, role)
            .apply()
    }

    fun obtenerToken(context: Context): String? {
        return context.getSharedPreferences(PREFS_NOMBRE, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)
    }

    fun obtenerUsername(context: Context): String? {
        return context.getSharedPreferences(PREFS_NOMBRE, Context.MODE_PRIVATE)
            .getString(KEY_USERNAME, null)
    }

    fun obtenerRole(context: Context): String? {
        return context.getSharedPreferences(PREFS_NOMBRE, Context.MODE_PRIVATE)
            .getString(KEY_ROLE, null)
    }

    fun esGestor(context: Context): Boolean {
        return obtenerRole(context)?.equals("GESTOR", ignoreCase = true) == true
    }

    fun cerrarSesion(context: Context) {
        context.getSharedPreferences(PREFS_NOMBRE, Context.MODE_PRIVATE).edit().clear().apply()
    }

    fun estaLogueado(context: Context): Boolean {
        return obtenerToken(context) != null
    }
}
