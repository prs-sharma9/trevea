package com.learn.android.trevea.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.android.trevea.data.model.Category
import com.learn.android.trevea.data.local.repository.UserPreferenceRepository
import com.learn.android.trevea.data.local.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
Todo:
 1. Refactor to remove logic of updating stats and keep this viewmodel to update username and profile photo only. This viewmodel will only read stats
 2. Create a new viewmodel for quiz screen, update user stats from that viewmodel
 3. Remove the user category list logic and code from the project

* */

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

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

    fun updatePhotoPath (path: String) {
        _profileUiState.update {
            it.copy(photoUrl = path)
        }
    }

//    Check todo section at the top
    fun updateLongestStreak (count: Int) {
        if (count > _profileUiState.value.longestStreak) {
            viewModelScope.launch(Dispatchers.IO) {
                userPreferenceRepository.updateLongestStreak(count)
            }
        }
    }

    //    Check todo section at the top
     fun increaseTotalQuestions () {
        viewModelScope.launch (Dispatchers.IO) {
            userPreferenceRepository.increaseTotalQuestion(_profileUiState.value.totalQuestions + 1)
        }
    }

    //    Check todo section at the top
    fun increaseTotalQuestionsAndCorrectAns () {
        viewModelScope.launch (Dispatchers.IO) {
            increaseTotalQuestions()
            userPreferenceRepository.increaseTotalCorrectAnswer(_profileUiState.value.totalCorrectAns + 1)
        }
    }


    fun toggleUserCategory(category: Category) {
        viewModelScope.launch (context = Dispatchers.IO) {
            if (userRepository.isCategorySelected(category.categoryId)) {
                userRepository.deleteUserCategory(
                    Category(
                        categoryId = category.categoryId,
                        categoryName = category.categoryName
                    )
                )
            } else {
                userRepository.insertUserCategory(
                    Category(
                        categoryId = category.categoryId,
                        categoryName =  category.categoryName
                    )
                )
            }
        }
    }

    fun saveUserProfile() {
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
//    val userCategories: List<Category> = emptyList(),
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