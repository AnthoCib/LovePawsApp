package com.cibertec.applovepaws.core.session

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private const val DATASTORE_NAME = "lovepaws_session"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

object SessionManager {

    private val KEY_TOKEN = stringPreferencesKey("token")
    private val KEY_USERNAME = stringPreferencesKey("username")

    fun guardarSesion(context: Context, token: String, username: String) {
        runBlocking {
            context.dataStore.edit { prefs: MutablePreferences ->
                prefs[KEY_TOKEN] = token
                prefs[KEY_USERNAME] = username
            }
        }
    }

    fun obtenerToken(context: Context): String? {
        return runBlocking {
            val preferences = context.dataStore.data.first()
            preferences[KEY_TOKEN]
        }
    }

    fun obtenerUsername(context: Context): String? {
        return runBlocking {
            val preferences = context.dataStore.data.first()
            preferences[KEY_USERNAME]
        }
    }

    fun cerrarSesion(context: Context) {
        runBlocking {
            context.dataStore.edit { prefs: MutablePreferences ->
                prefs.remove(KEY_TOKEN)
                prefs.remove(KEY_USERNAME)
            }
        }
    }

    fun estaLogueado(context: Context): Boolean {
        return !obtenerToken(context).isNullOrBlank()
    }
}

