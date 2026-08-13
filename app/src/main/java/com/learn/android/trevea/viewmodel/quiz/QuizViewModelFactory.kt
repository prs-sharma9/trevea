package com.learn.android.trevea.viewmodel.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learn.android.trevea.data.local.repository.UserPreferenceRepository
import com.learn.android.trevea.data.remote.repository.OtdbRepository

class QuizViewModelFactory(
    private val repository: OtdbRepository,
    private val preference: UserPreferenceRepository
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(repository, preference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}