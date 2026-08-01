package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundManager
import com.example.data.Question
import com.example.data.QuestionCategory
import com.example.data.QuestionsRepository
import com.example.data.ScoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import com.example.data.VehicleType

import com.example.data.AppLanguage

enum class QuizScreen {
    START, GAME, RESULT
}

enum class QuizMode(val labelUrdu: String, val labelEnglish: String, val iconEmoji: String) {
    FULL_TEST("🇵🇰 مکمل 50 ٹیسٹ (Full Exam)", "🇵🇰 Full 50 Test Exam", "🇵🇰"),
    MOTORCYCLE("🏍️ موٹر سائیکل لائسنس ٹیسٹ", "🏍️ Motorcycle License Test", "🏍️"),
    CAR_LTV("🚘 کار / ایل ٹی وی ٹیسٹ", "🚘 Car / LTV Test", "🚘"),
    TRUCK_HTV("🚚 ایچ ٹی وی / ٹرک / بس ٹیسٹ", "🚚 HTV / Truck / Bus Test", "🚚"),
    COMMERCIAL_BUILDER("🚜 کمرشل / ٹریکٹر / بلڈر ٹیسٹ", "🚜 Commercial / Tractor Test", "🚜"),
    MANDATORY_ONLY("🛑 لازمی نشانات ٹیسٹ", "🛑 Mandatory Signs Test", "🛑"),
    WARNING_ONLY("⚠️ انتباہی نشانات ٹیسٹ", "⚠️ Warning Signs Test", "⚠️"),
    GENERAL_KNOWLEDGE_ONLY("🧠 جنرل نالج و ٹریفک قوانین", "🧠 General Knowledge & Traffic Rules", "🧠");

    fun getLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.URDU -> labelUrdu
        AppLanguage.ENGLISH -> labelEnglish
        AppLanguage.ROMAN -> labelUrdu
    }
}

data class QuizUiState(
    val currentScreen: QuizScreen = QuizScreen.START,
    val selectedMode: QuizMode = QuizMode.FULL_TEST,
    val activeQuestions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val correctAnswersCount: Int = 0,
    val wrongAnswersCount: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswered: Boolean = false,
    val isCorrect: Boolean? = null,
    val currentShuffledOptions: List<String> = emptyList(),
    val currentCorrectIndex: Int = 0,
    val showAdDialog: Boolean = false,
    val adMessage: String = "Google AdMob Sponsored Break",
    val highScore: Int = 0,
    val isNewHighScore: Boolean = false,
    val isSoundEnabled: Boolean = true,
    val appLanguage: AppLanguage = AppLanguage.URDU,
    val adMobBannerId: String = ScoreRepository.DEFAULT_ADMOB_TEST_BANNER_ID
)

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val scoreRepo = ScoreRepository(application)
    private val soundManager = SoundManager(application)

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        val high = scoreRepo.getHighScore()
        val soundOn = scoreRepo.isSoundEnabled()
        val savedLangCode = scoreRepo.getAppLanguage()
        val initialLang = AppLanguage.values().find { it.code == savedLangCode } ?: AppLanguage.URDU
        val bannerId = scoreRepo.getAdMobBannerId()
        
        soundManager.isSoundEnabled = soundOn
        soundManager.setLanguage(initialLang)

        _uiState.update {
            it.copy(
                highScore = high,
                isSoundEnabled = soundOn,
                appLanguage = initialLang,
                adMobBannerId = bannerId
            )
        }
    }

    fun updateAdMobBannerId(newId: String) {
        scoreRepo.setAdMobBannerId(newId)
        val clean = scoreRepo.getAdMobBannerId()
        _uiState.update { it.copy(adMobBannerId = clean) }
    }

    fun selectMode(mode: QuizMode) {
        _uiState.update { it.copy(selectedMode = mode) }
    }

    fun selectLanguage(lang: AppLanguage) {
        scoreRepo.setAppLanguage(lang.code)
        soundManager.setLanguage(lang)
        
        val currentState = _uiState.value
        val currentQ = currentState.activeQuestions.getOrNull(currentState.currentQuestionIndex)
        val shuffled = if (currentQ != null) {
            val (opts, corrIdx) = prepareShuffledOptions(currentQ, lang)
            _uiState.update { state ->
                state.copy(
                    appLanguage = lang,
                    currentShuffledOptions = if (state.isAnswered) state.currentShuffledOptions else opts,
                    currentCorrectIndex = if (state.isAnswered) state.currentCorrectIndex else corrIdx
                )
            }
            opts
        } else {
            _uiState.update { state -> state.copy(appLanguage = lang) }
            emptyList()
        }

        if (_uiState.value.currentScreen == QuizScreen.GAME && _uiState.value.isSoundEnabled) {
            speakQuestion()
        }
    }

    fun toggleSound() {
        val newSoundState = !_uiState.value.isSoundEnabled
        soundManager.isSoundEnabled = newSoundState
        if (!newSoundState) {
            soundManager.stopSpeaking()
        } else {
            speakQuestion()
        }
        scoreRepo.setSoundEnabled(newSoundState)
        _uiState.update { it.copy(isSoundEnabled = newSoundState) }
    }

    fun speakQuestion() {
        val currentQ = _uiState.value.activeQuestions.getOrNull(_uiState.value.currentQuestionIndex) ?: return
        val textToSpeak = currentQ.getQuestionText(_uiState.value.appLanguage)
        soundManager.speakText(textToSpeak)
    }

    fun startGame() {
        val mode = _uiState.value.selectedMode
        val lang = _uiState.value.appLanguage
        val questionsList = when (mode) {
            QuizMode.FULL_TEST -> QuestionsRepository.questions
            QuizMode.MOTORCYCLE -> QuestionsRepository.questions.filter { it.vehicleType == VehicleType.MOTORCYCLE || it.vehicleType == VehicleType.ALL }
            QuizMode.CAR_LTV -> QuestionsRepository.questions.filter { it.vehicleType == VehicleType.CAR_LTV || it.vehicleType == VehicleType.ALL }
            QuizMode.TRUCK_HTV -> QuestionsRepository.questions.filter { it.vehicleType == VehicleType.TRUCK_HTV || it.vehicleType == VehicleType.ALL }
            QuizMode.COMMERCIAL_BUILDER -> QuestionsRepository.questions.filter { it.vehicleType == VehicleType.COMMERCIAL_BUILDER || it.vehicleType == VehicleType.ALL }
            QuizMode.MANDATORY_ONLY -> QuestionsRepository.questions.filter { it.category == QuestionCategory.MANDATORY }
            QuizMode.WARNING_ONLY -> QuestionsRepository.questions.filter { it.category == QuestionCategory.WARNING }
            QuizMode.GENERAL_KNOWLEDGE_ONLY -> QuestionsRepository.questions.filter { it.category == QuestionCategory.GENERAL_KNOWLEDGE }
        }

        val firstQuestion = questionsList.firstOrNull()
        val (shuffled, correctIndex) = prepareShuffledOptions(firstQuestion, lang)

        _uiState.update {
            it.copy(
                currentScreen = QuizScreen.GAME,
                activeQuestions = questionsList,
                currentQuestionIndex = 0,
                score = 0,
                correctAnswersCount = 0,
                wrongAnswersCount = 0,
                selectedOptionIndex = null,
                isAnswered = false,
                isCorrect = null,
                currentShuffledOptions = shuffled,
                currentCorrectIndex = correctIndex,
                showAdDialog = false,
                isNewHighScore = false
            )
        }

        speakQuestion()
    }

    private fun prepareShuffledOptions(question: Question?, lang: AppLanguage): Pair<List<String>, Int> {
        if (question == null) return Pair(emptyList(), 0)
        val originalOptions = question.getOptionsText(lang)
        val correctString = originalOptions.getOrNull(question.correctOptionIndex) ?: ""
        
        val shuffled = originalOptions.shuffled()
        val newCorrectIndex = shuffled.indexOf(correctString).let { if (it == -1) 0 else it }
        return Pair(shuffled, newCorrectIndex)
    }

    fun answerQuestion(selectedIndex: Int) {
        val currentState = _uiState.value
        if (currentState.isAnswered) return

        val isCorrect = selectedIndex == currentState.currentCorrectIndex
        val newScore = if (isCorrect) currentState.score + 10 else currentState.score
        val newCorrectCount = if (isCorrect) currentState.correctAnswersCount + 1 else currentState.correctAnswersCount
        val newWrongCount = if (!isCorrect) currentState.wrongAnswersCount + 1 else currentState.wrongAnswersCount

        if (isCorrect) {
            soundManager.playCorrectSound()
        } else {
            soundManager.playWrongSound()
        }

        _uiState.update {
            it.copy(
                selectedOptionIndex = selectedIndex,
                isAnswered = true,
                isCorrect = isCorrect,
                score = newScore,
                correctAnswersCount = newCorrectCount,
                wrongAnswersCount = newWrongCount
            )
        }
    }

    fun nextQuestion() {
        val currentState = _uiState.value
        val nextIdx = currentState.currentQuestionIndex + 1
        val totalQuestions = currentState.activeQuestions.size

        // Check if an AdMob demo interstitial should show (Every 5 questions answered: Q5, Q10, Q15...)
        val answeredSoFar = nextIdx
        val isAdTrigger = (answeredSoFar > 0 && answeredSoFar % 5 == 0 && nextIdx < totalQuestions)

        if (isAdTrigger && !currentState.showAdDialog) {
            _uiState.update { it.copy(showAdDialog = true) }
            return
        }

        if (nextIdx < totalQuestions) {
            val nextQ = currentState.activeQuestions[nextIdx]
            val (shuffled, correctIndex) = prepareShuffledOptions(nextQ, currentState.appLanguage)

            _uiState.update {
                it.copy(
                    currentQuestionIndex = nextIdx,
                    selectedOptionIndex = null,
                    isAnswered = false,
                    isCorrect = null,
                    currentShuffledOptions = shuffled,
                    currentCorrectIndex = correctIndex,
                    showAdDialog = false
                )
            }
            speakQuestion()
        } else {
            // Game Complete!
            finishGame()
        }
    }

    fun dismissAdDialog() {
        _uiState.update { it.copy(showAdDialog = false) }
        nextQuestion()
    }

    private fun finishGame() {
        val finalScore = _uiState.value.score
        val finalCorrect = _uiState.value.correctAnswersCount
        val isNewRecord = scoreRepo.saveHighScoreIfHigher(finalScore)
        scoreRepo.saveMaxCorrectIfHigher(finalCorrect)
        scoreRepo.incrementGamesPlayed()

        soundManager.playCompletionSound()

        val updatedHighScore = scoreRepo.getHighScore()

        _uiState.update {
            it.copy(
                currentScreen = QuizScreen.RESULT,
                highScore = updatedHighScore,
                isNewHighScore = isNewRecord
            )
        }
    }

    fun returnToStart() {
        _uiState.update {
            it.copy(
                currentScreen = QuizScreen.START,
                highScore = scoreRepo.getHighScore()
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }
}
