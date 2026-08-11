package com.learn.android.trevea.viewmodel.user

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

class UserViewModel(
    private val userRepository: UserRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterScreenUiState())
    val uiState: StateFlow<RegisterScreenUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

//    private fun loadUserCategories() {
//        viewModelScope.launch {
//            userRepository.userCategoryList.collect { userCategories ->
//                _uiState.update { it.copy(userCategories = userCategories) }
//            }
//        }
//    }

    private fun loadUserData() {
        viewModelScope.launch {
            combine(
                userPreferenceRepository.userName,
                userPreferenceRepository.photoUrl,
                userPreferenceRepository.isUserRegistered,
                userRepository.userCategoryList
            ) { name, photoUrl, isRegistered, userCategories ->
                RegisterScreenUiState(
                    name = name,
                    photoUrl = photoUrl,
                    isRegistered = isRegistered,
                    userCategories = userCategories
                )
            }.collect {
                state -> _uiState.update { state }
            }
        }
    }

    fun updateUserName (name: String) {
        _uiState.update {
            it.copy(name = name)
        }
    }

    fun updatePhotoPath (path: String) {
        _uiState.update {
            it.copy(photoUrl = path)
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
            val currentState = _uiState.value
            userPreferenceRepository.saveUserProfile(
                name = currentState.name,
                path = currentState.photoUrl
            )
            _uiState.update { it.copy(isSaved =  true) }
        }
    }
}

data class RegisterScreenUiState(
    val name: String = "",
    val photoUrl: String? = null,
    val isRegistered: Boolean = false,
    val userCategories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)