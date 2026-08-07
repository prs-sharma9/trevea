package com.learn.android.trevea.data.local.preferences

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)


object UserPreferences {
    const val USER_NAME_KEY = "user_name"
    const val PHOTO_URL_KEY = "photo_url"
    const val IS_USER_REGISTERED_KEY = "is_user_registered"

    val USER_NAME = stringPreferencesKey(USER_NAME_KEY)
    val PHOTO_PATH = stringPreferencesKey(PHOTO_URL_KEY)
    val IS_REGISTERED = booleanPreferencesKey(IS_USER_REGISTERED_KEY)
}