package com.learn.android.trevea.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.learn.android.trevea.R
import com.learn.android.trevea.data.remote.repository.OtdbRepository
import com.learn.android.trevea.data.remote.retrofit.RetrofitInstance
import com.learn.android.trevea.ui.components.CategoryList
import com.learn.android.trevea.ui.components.Error
import com.learn.android.trevea.ui.components.IconActionButton
import com.learn.android.trevea.ui.components.Loading
import com.learn.android.trevea.ui.components.TextActionButton
import com.learn.android.trevea.ui.components.TopAppBar
import com.learn.android.trevea.viewmodel.quiz.QuizViewModel
import com.learn.android.trevea.viewmodel.quiz.QuizViewModelFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import com.learn.android.trevea.data.remote.model.otdb.Question
import com.learn.android.trevea.ui.components.QuestionView
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val quizViewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(
            repository = OtdbRepository(
                api = RetrofitInstance.otdbApi
            )
        )
    )

    val uiState by quizViewModel.quizUiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            quizViewModel.stopPollingQuestions()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.select_topic),
                enableBackNavigation = true,
                navController = navController,
                enableActions = true,
                actions = {
                    IconActionButton (
                        icon = Icons.Default.Close,
                        description = stringResource(R.string.end_quiz_btn_descp)
                    ) {
                        navController.navigate("HomeScreen")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            when (uiState) {
                is QuizViewModel.QuizUiState.Loading -> {
                    Loading()
                }
                is QuizViewModel.QuizUiState.Error -> {
                    Error()
                }
                is QuizViewModel.QuizUiState.CategoryList -> {
                    CategoryList(
                        allCategories = (uiState as QuizViewModel.QuizUiState.CategoryList).categories,
                        onItemClick = { category ->
//                            quizViewModel.getContinuousQuestions()
                            quizViewModel.categoryId = category.categoryId
                            quizViewModel.getQuestions()
                        }
                    )
                }
                is QuizViewModel.QuizUiState.QuizMode -> {
                    var question by remember { mutableStateOf(quizViewModel.getNextQuestion()) }
                    QuestionView(
                        type = question.type,
                        question = question.question,
                        correctAnswer = question.correctAnswer,
                        incorrectAnswer = question.incorrectAnswers
                    ) {
                        question = quizViewModel.getNextQuestion()

                    }
                }
            }
        }
    }
}