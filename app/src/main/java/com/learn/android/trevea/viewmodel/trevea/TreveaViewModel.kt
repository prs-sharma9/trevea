package com.learn.android.trevea.viewmodel.trevea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.android.trevea.data.remote.model.otdb.Category
import com.learn.android.trevea.data.remote.repository.OtdbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TreveaViewModel(private val repository: OtdbRepository): ViewModel() {

    sealed class CategoryUiState {
        object Loading: CategoryUiState()
        data class Success(val categories: List<Category>): CategoryUiState()
        data class Error(val message: String): CategoryUiState()
    }

    private val _categoryUiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)

    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState.asStateFlow()

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            _categoryUiState.value = CategoryUiState.Loading

            val result = withContext(Dispatchers.IO) {
                repository.getAllCategories()
            }

            result.fold(
                onSuccess = { response ->
                    _categoryUiState.value = CategoryUiState.Success(response.triviaCategories)
                },
                onFailure = { exception ->
                    _categoryUiState.value = CategoryUiState.Error(exception.message ?: "Unknown error")
                }
            )
        }
    }
}