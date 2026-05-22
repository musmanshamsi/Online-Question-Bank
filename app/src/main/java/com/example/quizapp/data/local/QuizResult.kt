package com.example.quizapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val difficulty: String,
    val score: Int,
    val totalQuestions: Int,
    val dateTaken: Long
)
