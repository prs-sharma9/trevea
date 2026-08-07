package com.learn.android.trevea.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learn.android.trevea.data.local.repository.UserPreferenceRepository
import com.learn.android.trevea.data.local.repository.UserRepository

class UserViewModelFactory(
    private val repository: UserRepository,
    private val prefRepository: UserPreferenceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository = repository, userPreferenceRepository = prefRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}