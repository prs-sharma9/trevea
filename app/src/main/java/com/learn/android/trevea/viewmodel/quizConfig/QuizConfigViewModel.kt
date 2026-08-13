package com.learn.android.trevea.viewmodel.quizConfig

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.android.trevea.data.model.Category
import com.learn.android.trevea.data.remote.repository.OtdbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizConfigViewModel(
    private val repository: OtdbRepository
): ViewModel() {

    private val tag = "QuizConfigViewModel"

    sealed class QuizConfigUiState {
        object Loading: QuizConfigUiState()
        data class CategoryList(val categories: List<Category>): QuizConfigUiState()
        data class Error(val message: String): QuizConfigUiState()
    }


    private val _quizConfigUiState = MutableStateFlow<QuizConfigUiState>(QuizConfigUiState.Loading)

    val quizConfigUiState = _quizConfigUiState.asStateFlow()

    init {
        loadTopics()
    }

    private fun loadTopics() {
        viewModelScope.launch {
            _quizConfigUiState.value = QuizConfigUiState.Loading

            val result = withContext(Dispatchers.IO) {
                repository.getAllCategories()
            }

            result.fold(
                onSuccess = { response ->
                    Log.i(tag, "Load Category: Success: ${response.triviaCategories.size} categories loaded")
                    _quizConfigUiState.value = QuizConfigUiState.CategoryList(response.triviaCategories)
                },
                onFailure = { exception ->
                    _quizConfigUiState.value = QuizConfigUiState.Error("Error: ${exception.message}")
                }
            )
        }
    }

}