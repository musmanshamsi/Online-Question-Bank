package com.example.quizapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.quizapp.data.local.QuizDatabase
import com.example.quizapp.data.repository.QuizRepository
import com.example.quizapp.ui.screens.HistoryScreen
import com.example.quizapp.ui.screens.HomeScreen
import com.example.quizapp.ui.screens.QuizScreen
import com.example.quizapp.ui.screens.ResultScreen
import com.example.quizapp.ui.theme.QuizMasterTheme
import com.example.quizapp.viewmodel.HistoryViewModel
import com.example.quizapp.viewmodel.QuizViewModel
import com.example.quizapp.worker.QuizReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup notification channel and schedule WorkManager task
        createNotificationChannel()
        scheduleDailyReminder()

        // Initialize Local DB and Repository
        val database = QuizDatabase.getInstance(applicationContext)
        val repository = QuizRepository(database.quizResultDao())

        setContent {
            QuizMasterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Instantiate ViewModels using our provided factories
                    val quizViewModel: QuizViewModel = viewModel(
                        factory = QuizViewModel.provideFactory(repository)
                    )
                    val historyViewModel: HistoryViewModel = viewModel(
                        factory = HistoryViewModel.provideFactory(repository)
                    )

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                onStartQuiz = { categoryId, categoryName, difficulty ->
                                    quizViewModel.resetQuiz()
                                    navController.navigate("quiz/$categoryId/$difficulty/$categoryName")
                                },
                                onViewHistory = {
                                    navController.navigate("history")
                                }
                            )
                        }

                        composable(
                            route = "quiz/{categoryId}/{difficulty}/{categoryName}",
                            arguments = listOf(
                                navArgument("categoryId") { type = NavType.IntType },
                                navArgument("difficulty") { type = NavType.StringType },
                                navArgument("categoryName") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 18
                            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: "Easy"
                            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Computers"

                            QuizScreen(
                                viewModel = quizViewModel,
                                categoryId = categoryId,
                                categoryName = categoryName,
                                difficulty = difficulty,
                                onQuizCompleted = { score, total, catName, diff ->
                                    navController.navigate("result/$score/$total/$catName/$diff") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                },
                                onNavigateHome = {
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(
                            route = "result/{score}/{total}/{categoryName}/{difficulty}",
                            arguments = listOf(
                                navArgument("score") { type = NavType.IntType },
                                navArgument("total") { type = NavType.IntType },
                                navArgument("categoryName") { type = NavType.StringType },
                                navArgument("difficulty") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val score = backStackEntry.arguments?.getInt("score") ?: 0
                            val total = backStackEntry.arguments?.getInt("total") ?: 0
                            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Computers"
                            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: "Easy"

                            ResultScreen(
                                viewModel = quizViewModel,
                                score = score,
                                total = total,
                                categoryName = categoryName,
                                difficulty = difficulty,
                                onPlayAgain = {
                                    quizViewModel.resetQuiz()
                                    // Retrieve the original parameters to start quiz again
                                    val originalCategoryId = when (categoryName) {
                                        "General Knowledge" -> 9
                                        "Science & Nature" -> 17
                                        else -> 18 // Computers
                                    }
                                    navController.navigate("quiz/$originalCategoryId/$difficulty/$categoryName") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                },
                                onGoHome = {
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("history") {
                            HistoryScreen(
                                viewModel = historyViewModel,
                                onNavigateHome = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "quiz_reminders"
            val name = getString(R.string.notification_channel_name)
            val descriptionText = "Channel for daily quiz reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleDailyReminder() {
        val reminderRequest = PeriodicWorkRequestBuilder<QuizReminderWorker>(
            24, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "daily_quiz_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            reminderRequest
        )
    }
}
