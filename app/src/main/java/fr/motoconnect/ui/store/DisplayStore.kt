package fr.motoconnect.ui.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DisplayStore(private val context: Context) {

    companion object {
        private val Context.DataStore: DataStore<Preferences> by preferencesDataStore("display_store")
        private val DARK_MODE = booleanPreferencesKey("dark_mode")
    }

    val getDarkMode: Flow<Boolean> = context.DataStore.data.map { preferences ->
        preferences[DARK_MODE] ?: false
    }

    suspend fun setDarkMode(value: Boolean) {
        context.DataStore.edit { preferences ->
            preferences[DARK_MODE] = value
        }
    }

}