package com.learn.android.trevea.data.local.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.learn.android.trevea.data.local.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceRepository (
    private val dataStore: DataStore<Preferences>
) {

    val userName: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[UserPreferences.USER_NAME] ?: ""
        }

    val photoUrl: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[UserPreferences.PHOTO_PATH]
        }

    val isUserRegistered: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[UserPreferences.IS_REGISTERED] ?: false
        }


    suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[UserPreferences.USER_NAME] = name
        }
    }

    suspend fun savePhotoPath(path: String) {
        dataStore.edit { preferences ->
            preferences[UserPreferences.PHOTO_PATH] = path
        }
    }

    suspend fun saveUserProfile(path: String?, name: String) {
        dataStore.edit { preferences ->
            preferences[UserPreferences.USER_NAME] = name
            preferences[UserPreferences.IS_REGISTERED] = true
            path?.let {
                preferences[UserPreferences.PHOTO_PATH] = it
            }
        }
    }
}