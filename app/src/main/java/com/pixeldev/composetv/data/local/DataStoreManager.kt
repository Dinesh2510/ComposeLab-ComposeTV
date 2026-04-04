package com.pixeldev.composetv.data.local


import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("app_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
    }

    val onboardingDone: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_DONE] ?: false
        }

    suspend fun setOnboardingDone() {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_DONE] = true
        }
    }
}