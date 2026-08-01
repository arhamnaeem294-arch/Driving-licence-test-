package com.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ScoreRepository
import com.example.ui.components.AdMobBanner
import com.example.ui.screens.GameScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.StartScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.QuizScreen
import com.example.viewmodel.QuizViewModel
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
  private val viewModel: QuizViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    try {
      MobileAds.initialize(this) { initializationStatus ->
        Log.d("MainActivity", "AdMob initialized: $initializationStatus")
      }
    } catch (e: Exception) {
      Log.e("MainActivity", "AdMob init error", e)
    }

    setContent {
      val state by viewModel.uiState.collectAsStateWithLifecycle()
      var showAdIdDialog by remember { mutableStateOf(false) }

      MyApplicationTheme {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          bottomBar = {
            AdMobBanner(
              adUnitId = state.adMobBannerId,
              onOpenIdDialog = { showAdIdDialog = true }
            )
          }
        ) { innerPadding ->
          QuizAppContent(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
          )

          if (showAdIdDialog) {
            AdMobIdConfigDialog(
              currentId = state.adMobBannerId,
              onDismiss = { showAdIdDialog = false },
              onSave = { newId ->
                viewModel.updateAdMobBannerId(newId)
                showAdIdDialog = false
              }
            )
          }
        }
      }
    }
  }
}

@Composable
fun AdMobIdConfigDialog(
  currentId: String,
  onDismiss: () -> Unit,
  onSave: (String) -> Unit
) {
  var textInput by remember { mutableStateOf(currentId) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "⚙️ Configure AdMob Banner Unit ID",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
      )
    },
    text = {
      Column {
        Text(
          text = "Enter your custom Google AdMob Banner Ad Unit ID below (e.g. ca-app-pub-xxxxxxxxxxxx/xxxxxxxxxx):",
          fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
          value = textInput,
          onValueChange = { textInput = it },
          label = { Text("AdMob Banner Ad Unit ID") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("admob_id_input")
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
          textInput = ScoreRepository.DEFAULT_ADMOB_TEST_BANNER_ID
        }) {
          Text("Reset to Standard Google Test ID", fontSize = 12.sp)
        }
      }
    },
    confirmButton = {
      Button(onClick = { onSave(textInput) }) {
        Text("Save & Apply")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

@Composable
fun QuizAppContent(
  viewModel: QuizViewModel,
  modifier: Modifier = Modifier
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  when (state.currentScreen) {
    QuizScreen.START -> {
      StartScreen(
        state = state,
        onStartGame = { viewModel.startGame() },
        onSelectMode = { mode -> viewModel.selectMode(mode) },
        onSelectLanguage = { lang -> viewModel.selectLanguage(lang) },
        onToggleSound = { viewModel.toggleSound() },
        modifier = modifier
      )
    }
    QuizScreen.GAME -> {
      GameScreen(
        state = state,
        onAnswerSelected = { index -> viewModel.answerQuestion(index) },
        onNextQuestion = { viewModel.nextQuestion() },
        onDismissAd = { viewModel.dismissAdDialog() },
        onExitGame = { viewModel.returnToStart() },
        onToggleSound = { viewModel.toggleSound() },
        onSpeakQuestion = { viewModel.speakQuestion() },
        modifier = modifier
      )
    }
    QuizScreen.RESULT -> {
      ResultScreen(
        state = state,
        onPlayAgain = { viewModel.startGame() },
        onHome = { viewModel.returnToStart() },
        modifier = modifier
      )
    }
  }
}
