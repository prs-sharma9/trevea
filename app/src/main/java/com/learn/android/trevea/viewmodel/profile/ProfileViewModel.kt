package com.learn.android.trevea.viewmodel.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.android.trevea.data.local.repository.UserPreferenceRepository
import com.learn.android.trevea.utils.saveImageToInternalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    private val tag = "Trevea: ProfileViewModel"
    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _profileUiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val userInfoFlow = combine(
                userPreferenceRepository.userName,
                userPreferenceRepository.photoUrl,
                userPreferenceRepository.isUserRegistered,
            ) { name, photoUrl, isRegistered ->
                UserUiState(
                    name = name,
                    photoUrl = photoUrl,
                    isRegistered = isRegistered,
                )
            }


            val statsFlow = combine(
                userPreferenceRepository.longestStreak,
                userPreferenceRepository.totalQuestions,
                userPreferenceRepository.totalCorrectAns
            ) { longestStreak, totalQuestions, totalCorrectAns ->
                StatsUiState(
                    longestStreak = longestStreak,
                    totalQuestions = totalQuestions,
                    totalCorrectAns = totalCorrectAns
                )
            }


            combine(userInfoFlow, statsFlow) { userInfo: UserUiState, stats: StatsUiState ->
                ProfileUiState(
                    name = userInfo.name,
                    photoUrl = userInfo.photoUrl,
                    isRegistered = userInfo.isRegistered,
                    longestStreak = stats.longestStreak,
                    totalQuestions = stats.totalQuestions,
                    totalCorrectAns = stats.totalCorrectAns
                )
            }.collect { state: ProfileUiState ->
                _profileUiState.update { state }
            }
        }
    }

    fun updateUserName (name: String) {
        _profileUiState.update {
            it.copy(name = name)
        }
    }

    fun saveUserProfile (context: Context, path: Uri) {
        Log.d(tag, "updateProfilePhoto ${path.encodedPath}")
        viewModelScope.launch (Dispatchers.IO) {
            try {

            } catch (e: Exception) {

            }
            if (_profileUiState.value.photoUrl != null) {
                val oldFile = File(_profileUiState.value.photoUrl)
                if(oldFile.exists()) {
                    oldFile.delete()
                }
            }
            val savedPath = saveImageToInternalStorage(context = context, uri = path)
            savedPath?.let { path ->
                _profileUiState.update { it.copy( photoUrl = path ) }
            }
            userPreferenceRepository.saveUserProfile(
                name = _profileUiState.value.name,
                path = _profileUiState.value.photoUrl
            )
        }
    }

    fun saveUserProfile () {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _profileUiState.value
            userPreferenceRepository.saveUserProfile(
                name = currentState.name,
                path = currentState.photoUrl
            )
            _profileUiState.update { it.copy(isSaved =  true) }
        }
    }
}

data class UserUiState(
    val name: String = "",
    val photoUrl: String? = null,
    val isRegistered: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

data class StatsUiState (
    val longestStreak: Int = 0,
    val totalQuestions: Int = 0,
    val totalCorrectAns: Int = 0,
)

data class ProfileUiState (
    val name: String = "",
    val photoUrl: String? = null,
    val isRegistered: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val longestStreak: Int = 0,
    val totalQuestions: Int = 0,
    val totalCorrectAns: Int = 0
)