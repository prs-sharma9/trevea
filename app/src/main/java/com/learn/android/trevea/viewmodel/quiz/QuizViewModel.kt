package com.learn.android.trevea.viewmodel.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.android.trevea.data.model.Category
import com.learn.android.trevea.data.remote.model.otdb.Question
import com.learn.android.trevea.data.remote.repository.OtdbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizViewModel(private val repository: OtdbRepository): ViewModel() {

    sealed class QuizUiState {
        object Loading: QuizUiState()
        data class CategoryList(val categories: List<Category>): QuizUiState()
        object QuizMode: QuizUiState()
        data class Error(val message: String): QuizUiState()
    }

    private val _quizUiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)

    val quizUiState: StateFlow<QuizUiState> = _quizUiState.asStateFlow()
    private var pollingJob: Job? = null

    private val _displayedQuestions = MutableStateFlow<List<Question>>(emptyList())
//    val displayedQuestions: StateFlow<List<Question>> = _displayedQuestions.asStateFlow()

    private val _questionBuffer = MutableStateFlow<List<Question>>(emptyList())

    private val _currentQuestionIdx = MutableStateFlow(0)
//    val currentQuestionIdx = _currentQuestionIdx.asStateFlow()

    var categoryId: Int = 0

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            _quizUiState.value = QuizUiState.Loading

            val result = withContext(Dispatchers.IO) {
                repository.getAllCategories()
            }

            result.fold(
                onSuccess = { response ->
                    _quizUiState.value = QuizUiState.CategoryList(response.triviaCategories)
                },
                onFailure = { exception ->
                    _quizUiState.value = QuizUiState.Error(exception.message ?: "Unknown error")
                }
            )
        }
    }


    fun getQuestions(
        amount: Int = 10,
    ) {
        Log.i("Trevea Quiz:", "getQuestions()")
        viewModelScope.launch {
//            _quizUiState.value = QuizUiState.Loading

            val result = withContext(Dispatchers.IO) {
                repository.getQuestions(amount = amount, categoryId = categoryId)
            }

            result.fold(
                onSuccess = { response ->
                    Log.i("Trevea Quiz", "Polling: Success: ${response.questionList.size}")
                    _questionBuffer.value = response.questionList

                    _quizUiState.value = QuizUiState.QuizMode
                },
                onFailure = { exception ->
                    _quizUiState.value = QuizUiState.Error(exception.message ?: "Unknown Error")
                }
            )
        }
    }

    fun getContinuousQuestions(
        amount: Int = 10
    ) {
        pollingJob?.cancel()

        pollingJob = viewModelScope.launch {
            _quizUiState.value = QuizUiState.Loading
            repository.getContinuousQuestion(amount, categoryId)
                .collect { result ->
                    result.fold(
                        onSuccess = {questionList ->
                            Log.i("Trevea Quiz", "Polling: Success: ${questionList.size}")
                            _quizUiState.value = QuizUiState.QuizMode
                            _questionBuffer.value = questionList
                        },
                        onFailure = { error ->
                            Log.e("Trevea_Quiz", "error while loading: ${error.message}")
                            _quizUiState.value = QuizUiState.Error(
                                error.message ?: "Failed to load questions"
                            )
                        }
                    )
                }
        }
    }

    fun stopPollingQuestions() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun refreshQuestionList(): Boolean {
        Log.d("Trevea Quiz", "refreshing display list: buffer: ${_questionBuffer.value.size}")
        var status = false
        if(_questionBuffer.value.isNotEmpty()) {
            _displayedQuestions.value = _questionBuffer.value
            _questionBuffer.value = emptyList()
            _currentQuestionIdx.value = 0
            status = true
        } else {
            Log.e("Trevea Quiz: ", "Question Buffer empty")
        }
        viewModelScope.launch {
            delay(15000)
            getQuestions()
        }
        Log.i("Trevea Quiz: ", "refresh status: $status")
        return status
    }

    fun getNextQuestion(): Question {
        if (_currentQuestionIdx.value >= _displayedQuestions.value.size) {
            if(!refreshQuestionList()) {
                _quizUiState.value = QuizUiState.Error("Error while getting next question, Restart the quiz")
                return Question.default()
            }
        }
        Log.i("Trevea Quiz:", "Next question idx: ${_currentQuestionIdx.value}")

        return _displayedQuestions.value[_currentQuestionIdx.value].also { _currentQuestionIdx.value++ }
    }
}