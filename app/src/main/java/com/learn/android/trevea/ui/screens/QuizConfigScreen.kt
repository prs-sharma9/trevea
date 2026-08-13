package com.learn.android.trevea.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.learn.android.trevea.R
import com.learn.android.trevea.data.remote.repository.OtdbRepository
import com.learn.android.trevea.data.remote.retrofit.RetrofitInstance
import com.learn.android.trevea.ui.components.CategoryList
import com.learn.android.trevea.ui.components.Error
import com.learn.android.trevea.ui.components.Loading
import com.learn.android.trevea.ui.components.TopAppBar
import com.learn.android.trevea.viewmodel.quizConfig.QuizConfigViewModel
import com.learn.android.trevea.viewmodel.quizConfig.QuizConfigViewModelFactory

@Composable
fun QuizConfigScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val quizConfigVM: QuizConfigViewModel = viewModel(
        factory = QuizConfigViewModelFactory(
            repository = OtdbRepository(
                api = RetrofitInstance.otdbApi
            )
        )
    )

    val uiState = quizConfigVM.quizConfigUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier,
                title = stringResource(R.string.quiz_config_title),
                navController = navController,
                enableBackNavigation = true,
                enableActions = false,
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            when (uiState.value) {
                is QuizConfigViewModel.QuizConfigUiState.Loading -> {
                    Loading()
                }
                is QuizConfigViewModel.QuizConfigUiState.Error -> {
                    Error()
                }
                is QuizConfigViewModel.QuizConfigUiState.CategoryList -> {
                    CategoryList(
                        allCategories = (uiState.value as QuizConfigViewModel.QuizConfigUiState.CategoryList).categories,
                        onItemClick = { category ->
                            navController.navigate("quiz/${category.categoryId}")
                        }
                    )
                }
            }
        }
    }
}