package com.learn.android.trevea.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.android.trevea.data.local.model.user.UserCategory
import com.learn.android.trevea.data.local.repository.UserRepository
import com.learn.android.trevea.data.remote.model.otdb.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterScreenUiState())
    val uiState: StateFlow<RegisterScreenUiState> = _uiState.asStateFlow()

    init {
        loadUserCategories()
    }

    private fun loadUserCategories() {
        viewModelScope.launch {
            userRepository.userCategoryList.collect { userCategories ->
                _uiState.update { it.copy(userCategories = userCategories) }
            }
        }
    }


    fun toggleUserCategory(category: Category) {
        viewModelScope.launch (context = Dispatchers.IO) {
            if (userRepository.isCategorySelected(category.categoryId)) {
                userRepository.deleteUserCategory(
                    UserCategory(
                        categoryId = category.categoryId,
                        categoryName = category.categoryName
                    )
                )
            } else {
                userRepository.insertUserCategory(
                    UserCategory(
                        categoryId = category.categoryId,
                        categoryName =  category.categoryName
                    )
                )
            }
        }
    }

    fun saveUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaved =  true) }
        }
    }
}

data class RegisterScreenUiState(
    val userCategories: List<UserCategory> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)