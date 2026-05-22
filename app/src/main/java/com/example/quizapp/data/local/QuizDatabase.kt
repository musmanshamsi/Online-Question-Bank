package com.example.quizapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [QuizResult::class], version = 1, exportSchema = false)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun quizResultDao(): QuizResultDao

    companion object {
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        fun getDatabase(context: Context): QuizDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
