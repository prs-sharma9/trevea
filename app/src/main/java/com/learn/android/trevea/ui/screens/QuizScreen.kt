package com.learn.android.trevea.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.learn.android.trevea.ui.components.Error
import com.learn.android.trevea.ui.components.IconActionButton
import com.learn.android.trevea.ui.components.Loading
import com.learn.android.trevea.ui.components.TopAppBar
import com.learn.android.trevea.viewmodel.quiz.QuizViewModel
import com.learn.android.trevea.viewmodel.quiz.QuizViewModelFactory
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.learn.android.trevea.data.local.preferences.userDataStore
import com.learn.android.trevea.data.local.repository.UserPreferenceRepository
import com.learn.android.trevea.ui.components.QuestionView

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    categoryId: String? = "0"
) {

    val tag = "QuizScreen"
    val context = LocalContext.current
    val quizViewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(
            repository = OtdbRepository(
                api = RetrofitInstance.otdbApi,
            ),
            preference = UserPreferenceRepository(context.userDataStore)
        )
    )

    val uiState by quizViewModel.quizUiState.collectAsStateWithLifecycle()

    val answerState by quizViewModel.answerState.collectAsStateWithLifecycle()

    val question = quizViewModel.currentQuestion.collectAsStateWithLifecycle()

    var currentStreak by remember { mutableIntStateOf(0) }

    LaunchedEffect(categoryId) {
        var id: Int = 0
        try {
            id = categoryId?.toInt() ?: 0
        } catch (e: Exception) {
            Log.e(tag, "Invalid CategoryID: ${categoryId}")
        }
        quizViewModel.setCategory(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.select_topic),
                enableBackNavigation = false,
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
        },
        containerColor = Color.Transparent
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
                is QuizViewModel.QuizUiState.QuizMode -> {
                    QuestionView(
                        type = question.value?.type ?: "",
                        question = question.value?.question ?: "",
                        correctAnswer = question.value?.correctAnswer ?: "",
                        incorrectAnswer = question.value?.incorrectAnswers ?: emptyList(),
                        streak = currentStreak,
                        isOptionClickable = answerState == QuizViewModel.AnswerState.Idle
                    ) { isCorrectAns ->
                        if (isCorrectAns) currentStreak++ else currentStreak = 0
                        quizViewModel.onAnswerSelected(isCorrect = isCorrectAns, streak = currentStreak)
                    }
                }
            }
        }
    }
}