package com.example.echoguard.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

object SettingsKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
}

class SettingsDataStore(private val context: Context) {

    val darkModeFlow: Flow<Boolean> =
        context.dataStore.data.map {
            it[SettingsKeys.DARK_MODE] ?: false
        }

    suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit {
            it[SettingsKeys.DARK_MODE] = enabled
        }
    }
}