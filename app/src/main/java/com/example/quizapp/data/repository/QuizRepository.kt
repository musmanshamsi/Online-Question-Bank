package com.example.quizapp.data.repository

import com.example.quizapp.data.local.QuizResult
import com.example.quizapp.data.local.QuizResultDao
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.toQuestion
import com.example.quizapp.data.remote.QuizApiClient
import kotlinx.coroutines.flow.Flow

class QuizRepository(private val dao: QuizResultDao) {

    suspend fun getQuestions(categoryId: Int, difficulty: String): Result<List<Question>> {
        return QuizApiClient.fetchQuestions(categoryId, difficulty).map { apiQuestions ->
            apiQuestions.map { it.toQuestion() }
        }
    }

    suspend fun saveResult(result: QuizResult) {
        dao.insert(result)
    }

    fun getAllResults(): Flow<List<QuizResult>> {
        return dao.getAllResults()
    }
}
