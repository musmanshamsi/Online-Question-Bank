# Online Question Bank - Quiz App

A modern Android application built with Kotlin and Jetpack Compose that provides an interactive quiz experience with local data persistence and remote API integration.

## Features

- **Interactive Quiz Interface**: Clean and intuitive UI for answering multiple-choice questions
- **Score Tracking**: Keep track of your quiz scores with detailed result cards
- **Quiz History**: View your past quiz attempts and performance
- **Local Database**: SQLite-based local storage for offline access to quiz results
- **Remote API Integration**: Fetch quiz questions from a remote API
- **Push Notifications**: Get reminders to take quizzes through scheduled notifications
- **Material Design 3**: Modern UI components using Jetpack Compose

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/quizapp/
│   │   ├── MainActivity.kt                 # Main entry point
│   │   ├── data/
│   │   │   ├── local/                      # Local database layer
│   │   │   │   ├── QuizDatabase.kt
│   │   │   │   ├── QuizResult.kt
│   │   │   │   └── QuizResultDao.kt
│   │   │   ├── model/                      # Data models
│   │   │   │   └── Question.kt
│   │   │   ├── remote/                     # Remote API integration
│   │   │   │   ├── ApiModels.kt
│   │   │   │   └── QuizApiClient.kt
│   │   │   └── repository/                 # Data layer abstraction
│   │   │       └── QuizRepository.kt
│   │   ├── ui/
│   │   │   ├── components/                 # Reusable UI components
│   │   │   │   ├── AnswerButton.kt
│   │   │   │   └── ScoreCard.kt
│   │   │   ├── screens/                    # App screens
│   │   │   │   ├── HomeScreen.kt
│   │   │   │   ├── QuizScreen.kt
│   │   │   │   ├── ResultScreen.kt
│   │   │   │   └── HistoryScreen.kt
│   │   │   └── theme/                      # UI theme configuration
│   │   │       ├── Color.kt
│   │   │       ├── Theme.kt
│   │   │       └── Type.kt
│   │   ├── viewmodel/                      # ViewModel layer
│   │   │   ├── QuizViewModel.kt
│   │   │   └── HistoryViewModel.kt
│   │   └── worker/                         # Background tasks
│   │       └── QuizReminderWorker.kt
│   └── res/                                # Resources
│       └── values/
│           ├── strings.xml
│           └── themes.xml
├── build.gradle.kts                        # App-level build configuration
└── AndroidManifest.xml
```

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room (SQLite)
- **Networking**: Retrofit
- **Background Tasks**: WorkManager
- **Dependency Injection**: (Dagger/Hilt - if used)
- **Build System**: Gradle

## Getting Started

### Prerequisites

- Android Studio (latest version)
- Android SDK 21 or higher
- Gradle 7.0 or higher

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/musmanshamsi/Online-Question-Bank.git
   cd Online-Question-Bank
   ```

2. Open the project in Android Studio

3. Build the project:
   ```bash
   ./gradlew build
   ```

4. Run the app on an emulator or device:
   ```bash
   ./gradlew installDebug
   ```

## Configuration

Update the following files with your configuration:

- **API Endpoint**: Configure the remote API URL in `QuizApiClient.kt`
- **Database Configuration**: Modify database settings in `QuizDatabase.kt`
- **App Theme**: Customize colors and typography in the `theme/` folder

## Usage

1. **Home Screen**: Start your quiz session
2. **Quiz Screen**: Answer multiple-choice questions
3. **Result Screen**: View your score and performance
4. **History Screen**: Check your past quiz attempts

## Architecture

This app follows the **MVVM Architecture Pattern**:

- **Model**: Data models and repository pattern for data handling
- **View**: Compose UI components and screens
- **ViewModel**: Business logic and state management

### Data Flow

```
UI Layer (Composables)
    ↓
ViewModel Layer
    ↓
Repository Layer
    ↓
Data Sources (Local DB & Remote API)
```

## API Integration

The app integrates with a remote API to fetch quiz questions. Ensure your API endpoint is configured correctly in `QuizApiClient.kt`.

## Database

Quiz results are stored locally using Room database. The schema includes:
- Quiz result metadata
- User scores
- Timestamps

## Background Services

Quiz reminders are sent using **WorkManager** (`QuizReminderWorker.kt`) for reliable background task execution.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

**Usman Shamsi**
- GitHub: [@musmanshamsi](https://github.com/musmanshamsi)
- Email: m.usman.shamsi.pak@gmail.com

## Support

For support, email m.usman.shamsi.pak@gmail.com or open an issue on GitHub.

---

**Last Updated**: May 22, 2026
