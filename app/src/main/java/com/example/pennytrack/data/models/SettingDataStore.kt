package com.example.pennytrack.data.models

// SettingsDataStore.kt
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Create an extension property to initialize DataStore
val Context.dataStore by preferencesDataStore(name = "settings")

object PreferencesKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
}

suspend fun saveDarkMode(context: Context, isDarkMode: Boolean) {
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.DARK_MODE] = isDarkMode
    }
}

fun getDarkMode(context: Context): Flow<Boolean> = context.dataStore.data
    .catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }
    .map { preferences ->
        // Default to false (light mode) if no value is found
        preferences[PreferencesKeys.DARK_MODE] ?: false
    }
