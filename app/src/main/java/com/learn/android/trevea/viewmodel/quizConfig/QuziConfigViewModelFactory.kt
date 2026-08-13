package com.learn.android.trevea.viewmodel.quizConfig

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learn.android.trevea.data.remote.repository.OtdbRepository

class QuizConfigViewModelFactory(
    private val repository: OtdbRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizConfigViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizConfigViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}