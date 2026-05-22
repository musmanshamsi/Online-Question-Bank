package com.example.quizapp.data.remote

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object QuizApiClient {

    private const val BASE_URL = "https://opentdb.com/api.php"

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    suspend fun fetchQuestions(
        categoryId: Int,
        difficulty: String,
        amount: Int = 10
    ): Result<List<ApiQuestion>> = withContext(Dispatchers.IO) {
        try {
            val url = "$BASE_URL?amount=$amount&category=$categoryId&difficulty=$difficulty&type=multiple"

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    Exception("API request failed with code: ${response.code}")
                )
            }

            val responseBody = response.body?.string()
                ?: return@withContext Result.failure(Exception("Empty response body"))

            val triviaResponse = gson.fromJson(responseBody, TriviaApiResponse::class.java)

            if (triviaResponse.responseCode != 0) {
                return@withContext Result.failure(
                    Exception("API returned error code: ${triviaResponse.responseCode}")
                )
            }

            Result.success(triviaResponse.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
