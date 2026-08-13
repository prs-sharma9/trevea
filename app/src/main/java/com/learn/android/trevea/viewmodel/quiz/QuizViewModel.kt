package com.learn.android.trevea.viewmodel.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.android.trevea.data.local.repository.UserPreferenceRepository
import com.learn.android.trevea.data.remote.model.otdb.Question
import com.learn.android.trevea.data.remote.repository.OtdbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizViewModel(
    private val repository: OtdbRepository,
    private val preference: UserPreferenceRepository
): ViewModel() {

    private val tag = "QuizViewModel"
    sealed class QuizUiState {
        object Loading: QuizUiState()
        object QuizMode: QuizUiState()
        data class Error(val message: String): QuizUiState()
    }

    sealed class AnswerState {
        object Idle: AnswerState()
        data class Selected(
            val isCorrect: Boolean,
            val streak: Int
        ): AnswerState()
    }

    private val _quizUiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val quizUiState: StateFlow<QuizUiState> = _quizUiState.asStateFlow()
    private val _displayedQuestions = MutableStateFlow<List<Question>>(emptyList())
    private val _currentQuestionIdx = MutableStateFlow(0)
    private val _answerState = MutableStateFlow<AnswerState>(AnswerState.Idle)
    val answerState = _answerState.asStateFlow()
    private val _questionBuffer = MutableStateFlow<List<Question>>(emptyList())

    private var categoryId: Int = 0;

    val currentQuestion: StateFlow<Question?> = combine(
        _displayedQuestions.asStateFlow(),
        _currentQuestionIdx.asStateFlow()
    ) { questions, idx ->
        Log.d(tag, "Emitting question no: ${_currentQuestionIdx.value+1}")
        _answerState.value = AnswerState.Idle
        questions.getOrNull(idx)
    }.stateIn (
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Question.default()
    )

    private val _userStats = MutableStateFlow<UserStats>(UserStats())

    init {
        getUserStats()
    }

    fun setCategory(id: Int) {
        categoryId = id
        getQuestions()
    }
    private fun getUserStats() {
        viewModelScope.launch {
            combine(
                preference.longestStreak,
                preference.totalQuestions,
                preference.totalCorrectAns
            ) { longestStreak, totalQuestionCount, totalCorrectAnsCount ->
                UserStats(
                    longestStreak = longestStreak,
                    totalQuestion = totalQuestionCount,
                    totalCorrectAns = totalCorrectAnsCount
                )
            }.collect { stats ->
                Log.d(tag, "testing ${stats.longestStreak}")
                _userStats.update { stats }
            }
        }
    }

    private fun updateUserStats (
        streak: Int,
        isCorrectAns: Boolean,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(tag, "currentStreak: $streak, longestStreak: ${_userStats.value.longestStreak}")
            if (streak > _userStats.value.longestStreak) {
                preference.updateLongestStreak(streak)
            }

            if (isCorrectAns) {
                preference.increaseTotalCorrectAnswer()
            }
            preference.increaseTotalQuestion()
        }
    }


    fun onAnswerSelected(isCorrect: Boolean, streak: Int) {

        Log.d(tag,  "onAnswerSelected")
        if (_answerState.value !is AnswerState.Idle) return

        viewModelScope.launch {
            _answerState.value = AnswerState.Selected(isCorrect = isCorrect, streak = streak)

            updateUserStats(streak = streak, isCorrectAns = isCorrect)


            delay(2000)
            Log.d(tag, "currentIdx: ${_currentQuestionIdx.value}, totalQuestion: ${_displayedQuestions.value.size}")

            if (_currentQuestionIdx.value + 1 >= _displayedQuestions.value.size) {
                if (!refreshQuestionList()) {
                    _quizUiState.value = QuizUiState.Error("Something went wrong, please restart quiz")
                }
            } else {
                _currentQuestionIdx.value ++
            }


        }

    }

    fun getQuestions(
        amount: Int = 10,
    ) {
        Log.i(tag, "getQuestions()")
        viewModelScope.launch {
//            _quizUiState.value = QuizUiState.Loading

            val result = withContext(Dispatchers.IO) {
                repository.getQuestions(amount = amount, categoryId = categoryId)
            }

            result.fold(
                onSuccess = { response ->
                    Log.i(tag, "Polling: Success: ${response.questionList.size}")
                    _questionBuffer.value = response.questionList
                    if (_displayedQuestions.value.isEmpty()) {
                        refreshQuestionList()
                    }

                    _quizUiState.value = QuizUiState.QuizMode
                },
                onFailure = { exception ->
                    _quizUiState.value = QuizUiState.Error(exception.message ?: "Unknown Error")
                }
            )
        }
    }




    private fun refreshQuestionList(): Boolean {
        Log.d(tag, "refreshing display list: buffer: ${_questionBuffer.value.size}")
        var status = false
        if(_questionBuffer.value.isNotEmpty()) {
            _displayedQuestions.value = _questionBuffer.value
            _questionBuffer.value = emptyList()
            _currentQuestionIdx.value = 0
            status = true
        } else {
            Log.e(tag, "Question Buffer empty")
        }
        viewModelScope.launch {
            delay(15000)
            getQuestions()
        }
        Log.i(tag, "refresh status: $status")
        return status
    }

    fun getNextQuestion(): Question {
        if (_currentQuestionIdx.value >= _displayedQuestions.value.size) {
            if(!refreshQuestionList()) {
                _quizUiState.value = QuizUiState.Error("Error while getting next question, Restart the quiz")
                return Question.default()
            }
        }
        Log.i(tag, "Next question idx: ${_currentQuestionIdx.value}")

        return _displayedQuestions.value[_currentQuestionIdx.value].also { _currentQuestionIdx.value++ }
    }
}

data class UserStats(
    val longestStreak: Int = 0,
    val totalQuestion: Int = 0,
    val totalCorrectAns: Int = 0
)