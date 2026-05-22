package com.example.quizapp.data.model

import android.os.Build
import android.text.Html
import com.example.quizapp.data.remote.ApiQuestion

data class Question(
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val allAnswers: List<String>
)

fun ApiQuestion.toQuestion(): Question {
    val decodedQuestion = decodeHtml(question)
    val decodedCorrect = decodeHtml(correctAnswer)
    val decodedIncorrect = incorrectAnswers.map { decodeHtml(it) }

    val allAnswers = (decodedIncorrect + decodedCorrect).shuffled()

    return Question(
        question = decodedQuestion,
        correctAnswer = decodedCorrect,
        incorrectAnswers = decodedIncorrect,
        allAnswers = allAnswers
    )
}

private fun decodeHtml(html: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(html).toString()
    }
}
