package com.learn.android.trevea.data.local.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.learn.android.trevea.data.local.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceRepository (
    private val dataStore: DataStore<Preferences>
) {

    private val tag = "UserPreferenceRepository"
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

    val longestStreak: Flow<Int> = dataStore.data
        .map { preference ->
            preference[UserPreferences.LONGEST_STREAK] ?: 0
        }

    val totalQuestions: Flow<Int> = dataStore.data
        .map { preference ->
            preference[UserPreferences.TOTAL_QUESTION_COUNT] ?: 0
        }

    val totalCorrectAns: Flow<Int> = dataStore.data
        .map { preference ->
            preference[UserPreferences.TOTAL_CORRECT_ANSWER] ?: 0
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

    suspend fun updateLongestStreak(count: Int) {
        Log.d(tag, "updateLongestStreak, count: ${count}")
        dataStore.edit { preference ->
            if (count > preference[UserPreferences.LONGEST_STREAK] ?: 0)
            preference[UserPreferences.LONGEST_STREAK] = count
        }
    }

    suspend fun increaseTotalQuestion() {
        dataStore.edit { preference ->
            var count = preference[UserPreferences.TOTAL_QUESTION_COUNT] ?: 0
            Log.d(tag, "increaseTotalQuestion, count: ${count + 1}")
            preference[UserPreferences.TOTAL_QUESTION_COUNT] = count + 1
        }
    }

    suspend fun increaseTotalCorrectAnswer() {
        dataStore.edit { preference ->
            var count = preference[UserPreferences.TOTAL_CORRECT_ANSWER] ?: 0
            Log.d(tag, "increaseTotalCorrectAnswer, count: ${count + 1}")
            preference[UserPreferences.TOTAL_CORRECT_ANSWER] = count + 1
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