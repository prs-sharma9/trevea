package com.learn.android.trevea.viewmodel.stats

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.android.trevea.data.local.repository.UserPreferenceRepository
import com.learn.android.trevea.viewmodel.profile.StatsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserStatsViewModel (
    userPreferenceRepository: UserPreferenceRepository
): ViewModel() {


    var _statsUiState = MutableStateFlow(StatsUiState())

    val statsUiState = _statsUiState.asStateFlow()

    init {
        loadStatsData()
    }

    private fun loadStatsData() {
        viewModelScope.launch {

        }
    }

}

data class StatsUiState (
    val longestStreak: Int = 0,
    val totalQuestions: Int = 0,
    val totalCorrectAns: Int = 0,
)