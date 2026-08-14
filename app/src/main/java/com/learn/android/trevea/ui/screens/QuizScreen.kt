package com.learn.android.trevea.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.learn.android.trevea.ui.components.Loading
import com.learn.android.trevea.ui.components.TopAppBar
import com.learn.android.trevea.viewmodel.quiz.QuizViewModel
import com.learn.android.trevea.viewmodel.quiz.QuizViewModelFactory
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
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
                title = {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, end = 10.dp),
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = stringResource(R.string.streak_icon_description),
                            tint = colorResource(R.color.streak_flame)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "$currentStreak",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                enableBackNavigation = true,
                navController = navController,
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
//                .border(2.dp, Color.Black)
        ) {
            when (uiState) {
                is QuizViewModel.QuizUiState.Loading -> {
                    Loading()
                }
                is QuizViewModel.QuizUiState.Error -> {
                    Log.e(tag, "Error State")
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