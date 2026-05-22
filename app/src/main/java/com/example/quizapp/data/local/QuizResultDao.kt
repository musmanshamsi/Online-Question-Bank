package com.example.quizapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: QuizResult)

    @Query("SELECT * FROM quiz_results ORDER BY dateTaken DESC")
    fun getAllResults(): Flow<List<QuizResult>>
}
