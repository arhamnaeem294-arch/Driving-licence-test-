package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import com.example.ui.components.TrafficSignCard
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.CorrectGreenContainer
import com.example.ui.theme.PakGoldAccent
import com.example.ui.theme.PakGreenDark
import com.example.ui.theme.PakGreenPrimary
import com.example.ui.theme.WrongRed
import com.example.ui.theme.WrongRedContainer
import com.example.viewmodel.QuizUiState

@Composable
fun GameScreen(
    state: QuizUiState,
    onAnswerSelected: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onDismissAd: () -> Unit,
    onExitGame: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val currentQuestion = state.activeQuestions.getOrNull(state.currentQuestionIndex) ?: return

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // TOP HEADER BAR: Exit | question 1/50 | Sound Toggle | Score
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = PakGreenPrimary,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onExitGame,
                            modifier = Modifier.testTag("exit_game_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Exit",
                                tint = Color.White
                            )
                        }

                        // Question Counter
                        Text(
                            text = when (state.appLanguage) {
                                com.example.data.AppLanguage.URDU -> "سوال ${state.currentQuestionIndex + 1}/${state.activeQuestions.size}"
                                com.example.data.AppLanguage.ENGLISH -> "Question ${state.currentQuestionIndex + 1}/${state.activeQuestions.size}"
                                com.example.data.AppLanguage.ROMAN -> "Sawal ${state.currentQuestionIndex + 1}/${state.activeQuestions.size}"
                            },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.testTag("question_counter_text")
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Sound Toggle Button
                            IconButton(
                                onClick = onToggleSound,
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .testTag("toggle_sound_game_button")
                            ) {
                                Icon(
                                    imageVector = if (state.isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                    contentDescription = "Sound Toggle",
                                    tint = Color.White
                                )
                            }

                            // Score Badge
                            Surface(
                                color = PakGoldAccent,
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.testTag("score_badge")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Stars,
                                        contentDescription = "Score",
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${state.score}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Progress Bar
                    val progress = (state.currentQuestionIndex + 1).toFloat() / state.activeQuestions.size.toFloat()
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = PakGoldAccent,
                        trackColor = Color.White.copy(alpha = 0.3f),
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Category Pill
                    Text(
                        text = "${currentQuestion.category.getPartName(state.appLanguage)}: ${currentQuestion.category.getTitle(state.appLanguage)}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            // MIDDLE SCROLLABLE CONTENT (Sign & Question & Options)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // VISUAL SIGN BANNER / BADGE
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🖼️", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (state.appLanguage) {
                                com.example.data.AppLanguage.URDU -> "نشان دیکھیں اور جواب دیں (Traffic Sign):"
                                com.example.data.AppLanguage.ENGLISH -> "Observe Traffic Sign & Choose Answer:"
                                com.example.data.AppLanguage.ROMAN -> "Nishan dekhein aur jawab dein:"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // TRAFFIC SIGN GRAPHIC / EMOJI CARD
                TrafficSignCard(
                    question = currentQuestion,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(vertical = 4.dp)
                        .testTag("traffic_sign_card")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // QUESTION CARD WITH SPEECH READOUT BUTTON
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentQuestion.getQuestionText(state.appLanguage),
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Start,
                                lineHeight = 26.sp,
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // TTS SPEAKER BUTTON (🔊 sawal sunein)
                            Surface(
                                onClick = onSpeakQuestion,
                                color = if (state.isSoundEnabled) PakGreenPrimary else Color.Gray,
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(44.dp)
                                    .shadow(4.dp, CircleShape)
                                    .testTag("speak_question_button")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Speak Question",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = currentQuestion.questionRoman,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 4 LARGE ANSWER BUTTONS WITH HIGH-CONTRAST BOX CONTAINERS
                state.currentShuffledOptions.forEachIndexed { index, optionText ->
                    val isSelected = state.selectedOptionIndex == index
                    val isCorrectIndex = index == state.currentCorrectIndex

                    val optionBoxBg = when {
                        !state.isAnswered -> Color(0xFFF8FAFC)
                        isCorrectIndex -> CorrectGreenContainer
                        isSelected -> WrongRedContainer
                        else -> Color(0xFFF1F5F9)
                    }

                    val optionBoxBorder = when {
                        !state.isAnswered -> if (isSelected) PakGreenPrimary else Color(0xFF94A3B8)
                        isCorrectIndex -> CorrectGreen
                        isSelected -> WrongRed
                        else -> Color(0xFFCBD5E1)
                    }

                    val optionTextColor = when {
                        !state.isAnswered -> Color(0xFF001F3F) // Deep Navy Blue for maximum legibility
                        isCorrectIndex -> Color(0xFF065F46) // Dark Emerald Green
                        isSelected -> Color(0xFF991B1B) // Deep Red
                        else -> Color(0xFF334155) // Slate Gray
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .testTag("option_button_$index")
                            .clickable(enabled = !state.isAnswered) {
                                onAnswerSelected(index)
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = optionBoxBg),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(optionBoxBorder),
                            width = if (!state.isAnswered || isCorrectIndex || isSelected) 2.dp else 1.dp
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = when {
                                        state.isAnswered && isCorrectIndex -> CorrectGreen
                                        state.isAnswered && isSelected -> WrongRed
                                        else -> PakGreenPrimary
                                    },
                                    shape = CircleShape,
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${index + 1}",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Text(
                                    text = optionText,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = optionTextColor,
                                    lineHeight = 24.sp
                                )
                            }

                            if (state.isAnswered) {
                                if (isCorrectIndex) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = CorrectGreen,
                                        modifier = Modifier.size(26.dp)
                                    )
                                } else if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Wrong",
                                        tint = WrongRed,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // LEARNING TIP / EXPLANATION CARD WHEN ANSWERED
                AnimatedVisibility(
                    visible = state.isAnswered,
                    enter = fadeIn() + slideInVertically()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (state.isCorrect == true) CorrectGreenContainer.copy(alpha = 0.5f) else WrongRedContainer.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Tip",
                                tint = if (state.isCorrect == true) CorrectGreen else WrongRed,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (state.isCorrect == true) {
                                        when (state.appLanguage) {
                                            com.example.data.AppLanguage.URDU -> "🟢 صحیح جواب! (+10 Score)"
                                            com.example.data.AppLanguage.ENGLISH -> "🟢 Correct Answer! (+10 Score)"
                                            com.example.data.AppLanguage.ROMAN -> "🟢 Sahi Jawab! (+10 Score)"
                                        }
                                    } else {
                                        when (state.appLanguage) {
                                            com.example.data.AppLanguage.URDU -> "🔴 غلط جواب (0 Score)"
                                            com.example.data.AppLanguage.ENGLISH -> "🔴 Incorrect Answer (0 Score)"
                                            com.example.data.AppLanguage.ROMAN -> "🔴 Galat Jawab (0 Score)"
                                        }
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (state.isCorrect == true) CorrectGreen else WrongRed
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentQuestion.getExplanationText(state.appLanguage),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1E293B),
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }

            // BOTTOM ACTION BAR: NEXT QUESTION BUTTON
            if (state.isAnswered) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Button(
                        onClick = onNextQuestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(54.dp)
                            .testTag("next_question_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PakGreenPrimary)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (state.currentQuestionIndex + 1 < state.activeQuestions.size) {
                                    when (state.appLanguage) {
                                        com.example.data.AppLanguage.URDU -> "اگلا سوال (Next Question)"
                                        com.example.data.AppLanguage.ENGLISH -> "Next Question ➡️"
                                        com.example.data.AppLanguage.ROMAN -> "Agla Sawal (Next Question)"
                                    }
                                } else {
                                    when (state.appLanguage) {
                                        com.example.data.AppLanguage.URDU -> "نتیجہ دیکھیں (View Results)"
                                        com.example.data.AppLanguage.ENGLISH -> "View Final Result 🏁"
                                        com.example.data.AppLanguage.ROMAN -> "Natija Dekhein (View Results)"
                                    }
                                },
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        // ADMOB DEMO INTERSTITIAL DIALOG (Triggers every 5 questions: "5 Sawal ke baad = Ad dikhega")
        if (state.showAdDialog) {
            AlertDialog(
                onDismissRequest = onDismissAd,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "📺 ", fontSize = 22.sp)
                        Text(
                            text = "ایڈ بریک (AdMob Interstitial Demo)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PakGreenPrimary
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            text = "آپ نے 5 سوالات مکمل کر لیے ہیں!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E293B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Google AdMob Sponsored Ad",
                                    color = PakGoldAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "احتیاط سے ڈرائیو کریں، محفوظ رہیں!",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = onDismissAd,
                        colors = ButtonDefaults.buttonColors(containerColor = PakGreenPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("continue_game_ad_button")
                    ) {
                        Text(text = "کھیل جاری رکھیں (Continue Game)")
                    }
                }
            )
        }
    }
}
