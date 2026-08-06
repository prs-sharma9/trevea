package com.learn.android.trevea.viewmodel.trevea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learn.android.trevea.data.remote.repository.OtdbRepository

class TreveaViewModelFactory(
    private val repository: OtdbRepository
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TreveaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TreveaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}