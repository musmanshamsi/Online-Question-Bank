package com.example.quizapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.local.QuizResult
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizUiState(
    val isLoading: Boolean = false,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: String? = null,
    val isAnswerSelected: Boolean = false,
    val score: Int = 0,
    val error: String? = null,
    val isQuizCompleted: Boolean = false,
    val categoryName: String = "",
    val difficulty: String = ""
) {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)
}

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    // Session cache to prevent re-fetching when navigating
    private var cachedQuestions: List<Question>? = null
    private var cachedCategory: Int? = null
    private var cachedDifficulty: String? = null

    fun loadQuestions(categoryId: Int, categoryName: String, difficulty: String) {
        // If we already have questions for this exact selection, do not re-fetch
        if (cachedQuestions != null && cachedCategory == categoryId && cachedDifficulty == difficulty) {
            _uiState.update {
                QuizUiState(
                    questions = cachedQuestions ?: emptyList(),
                    categoryName = categoryName,
                    difficulty = difficulty
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            repository.getQuestions(categoryId, difficulty.lowercase())
                .onSuccess { questions ->
                    if (questions.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "No questions found for this configuration."
                            )
                        }
                    } else {
                        cachedQuestions = questions
                        cachedCategory = categoryId
                        cachedDifficulty = difficulty

                        _uiState.update {
                            QuizUiState(
                                questions = questions,
                                categoryName = categoryName,
                                difficulty = difficulty
                            )
                        }
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "An unexpected error occurred"
                        )
                    }
                }
        }
    }

    fun selectAnswer(answer: String) {
        val state = _uiState.value
        if (state.isAnswerSelected || state.currentQuestion == null) return

        val isCorrect = state.currentQuestion.correctAnswer == answer
        _uiState.update {
            it.copy(
                selectedAnswer = answer,
                isAnswerSelected = true,
                score = if (isCorrect) state.score + 1 else state.score
            )
        }
    }

    fun moveToNextQuestion() {
        val state = _uiState.value
        val nextIndex = state.currentQuestionIndex + 1

        if (nextIndex >= state.questions.size) {
            _uiState.update {
                it.copy(isQuizCompleted = true)
            }
        } else {
            _uiState.update {
                it.copy(
                    currentQuestionIndex = nextIndex,
                    selectedAnswer = null,
                    isAnswerSelected = false
                )
            }
        }
    }

    fun saveQuizResult() {
        val state = _uiState.value
        if (state.questions.isEmpty()) return

        viewModelScope.launch {
            val result = QuizResult(
                category = state.categoryName,
                difficulty = state.difficulty,
                score = state.score,
                totalQuestions = state.questions.size,
                dateTaken = System.currentTimeMillis()
            )
            repository.saveResult(result)
        }
    }

    fun resetQuiz() {
        cachedQuestions = null
        cachedCategory = null
        cachedDifficulty = null
        _uiState.value = QuizUiState()
    }

    companion object {
        fun provideFactory(repository: QuizRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return QuizViewModel(repository) as T
            }
        }
    }
}
