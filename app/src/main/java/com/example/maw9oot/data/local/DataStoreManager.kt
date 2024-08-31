package com.example.maw9oot.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")

    private val LANGUAGE_KEY = stringPreferencesKey("language")

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_THEME_KEY] ?: false
        }

    val language : Flow<String> = context.dataStore.data
        .map{ preferences->
            preferences[LANGUAGE_KEY] ?: "en"
        }

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDarkTheme
        }
    }

    suspend fun setLanguage(language: String){
        context.dataStore.edit{ preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }
}