package com.example.quizapp.data.remote

import com.google.gson.annotations.SerializedName

data class TriviaApiResponse(
    @SerializedName("response_code") val responseCode: Int,
    @SerializedName("results") val results: List<ApiQuestion>
)

data class ApiQuestion(
    @SerializedName("question") val question: String,
    @SerializedName("correct_answer") val correctAnswer: String,
    @SerializedName("incorrect_answers") val incorrectAnswers: List<String>
)
