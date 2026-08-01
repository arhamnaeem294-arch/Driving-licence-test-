package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.CorrectGreenContainer
import com.example.ui.theme.PakGoldAccent
import com.example.ui.theme.PakGreenDark
import com.example.ui.theme.PakGreenPrimary
import com.example.ui.theme.WrongRed
import com.example.viewmodel.QuizUiState

private data class GradeData(
    val title: String,
    val badgeText: String,
    val color: Color,
    val emoji: String
)

@Composable
fun ResultScreen(
    state: QuizUiState,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val totalQuestions = state.activeQuestions.size
    val correctCount = state.correctAnswersCount
    val wrongCount = state.wrongAnswersCount
    val percentage = if (totalQuestions > 0) ((correctCount.toFloat() / totalQuestions.toFloat()) * 100).toInt() else 0

    // Result status logic (Pass >= 75%, Average 50-74%, Fail < 50%)
    val grade = when {
        percentage >= 75 -> when (state.appLanguage) {
            AppLanguage.URDU -> GradeData("کامیاب! مبارک ہو 🎉", "پاس (PASSED)", CorrectGreen, "🏆")
            AppLanguage.ENGLISH -> GradeData("Passed! Congratulations 🎉", "PASSED", CorrectGreen, "🏆")
            AppLanguage.ROMAN -> GradeData("Kamyab! Mubarak ho 🎉", "PASSED", CorrectGreen, "🏆")
        }
        percentage >= 50 -> when (state.appLanguage) {
            AppLanguage.URDU -> GradeData("اوسط کارکردگی! 📊", "اوسط (AVERAGE)", Color(0xFFFF8F00), "⚖️")
            AppLanguage.ENGLISH -> GradeData("Average Performance 📊", "AVERAGE", Color(0xFFFF8F00), "⚖️")
            AppLanguage.ROMAN -> GradeData("Osat Performance! 📊", "AVERAGE", Color(0xFFFF8F00), "⚖️")
        }
        else -> when (state.appLanguage) {
            AppLanguage.URDU -> GradeData("ناکام! دوبارہ کوشش کریں 🚘", "ناکام (FAILED)", WrongRed, "📉")
            AppLanguage.ENGLISH -> GradeData("Failed! Please Try Again 🚘", "FAILED", WrongRed, "📉")
            AppLanguage.ROMAN -> GradeData("Nakam! Dobara koshish karein 🚘", "FAILED", WrongRed, "📉")
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Emoji Icon Header
            Surface(
                color = grade.color.copy(alpha = 0.15f),
                shape = CircleShape,
                modifier = Modifier
                    .size(90.dp)
                    .shadow(6.dp, CircleShape)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = grade.emoji,
                        fontSize = 48.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TITLE
            Text(
                text = grade.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = grade.color,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Main Score Text
            Text(
                text = when (state.appLanguage) {
                    AppLanguage.URDU -> "آپ نے $totalQuestions میں سے $correctCount سوالات درست کیے"
                    AppLanguage.ENGLISH -> "You answered $correctCount out of $totalQuestions questions correctly"
                    AppLanguage.ROMAN -> "Aap ne $totalQuestions me se $correctCount sawal sahi kiye"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("final_result_summary_text")
            )

            Text(
                text = "Score Accuracy: $percentage%",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // SCORE BADGE & PASS / AVERAGE / FAIL STATUS CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = grade.color.copy(alpha = 0.08f)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(grade.color, PakGreenPrimary)
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (state.appLanguage) {
                            AppLanguage.URDU -> "کل اسکور: ${state.score}"
                            AppLanguage.ENGLISH -> "Total Score: ${state.score}"
                            AppLanguage.ROMAN -> "Kul Score: ${state.score}"
                        },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = grade.color
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = grade.color,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = grade.badgeText,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }

                    if (state.isNewHighScore) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = when (state.appLanguage) {
                                AppLanguage.URDU -> "🌟 نیا اعلیٰ ترین سکور (New High Score)! 🌟"
                                AppLanguage.ENGLISH -> "🌟 New Record High Score! 🌟"
                                AppLanguage.ROMAN -> "🌟 Naya High Score! 🌟"
                            },
                            color = PakGoldAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // BREAKDOWN STATS CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = when (state.appLanguage) {
                            AppLanguage.URDU -> "کارکردگی کی تفصیل (Performance Breakdown):"
                            AppLanguage.ENGLISH -> "Performance Breakdown:"
                            AppLanguage.ROMAN -> "Karkardagi ki tafseel:"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = PakGreenPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(
                            label = when (state.appLanguage) {
                                AppLanguage.URDU -> "درست"
                                AppLanguage.ENGLISH -> "Correct"
                                AppLanguage.ROMAN -> "Sahi"
                            },
                            value = "🟢 $correctCount"
                        )
                        StatItem(
                            label = when (state.appLanguage) {
                                AppLanguage.URDU -> "غلط"
                                AppLanguage.ENGLISH -> "Incorrect"
                                AppLanguage.ROMAN -> "Galat"
                            },
                            value = "🔴 $wrongCount"
                        )
                        StatItem(
                            label = when (state.appLanguage) {
                                AppLanguage.URDU -> "فیصد"
                                AppLanguage.ENGLISH -> "Accuracy"
                                AppLanguage.ROMAN -> "Fisad"
                            },
                            value = "$percentage%"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = when (state.appLanguage) {
                                AppLanguage.URDU -> "ملاخطہ: درست جواب پر +10 اسکور اور غلط جواب پر 0 اسکور دیا گیا ہے۔"
                                AppLanguage.ENGLISH -> "Note: Correct answer gives +10 Score, incorrect answer gives 0 Score."
                                AppLanguage.ROMAN -> "Note: Sahi jawab par +10 Score aur galat jawab par 0 Score hai."
                            },
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ACTION BUTTONS: دوبارہ کھیلیں (Play Again) + دوستوں کو شیئر کریں (Share)
            Button(
                onClick = onPlayAgain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .testTag("play_again_button")
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PakGreenPrimary)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = "Retake Test",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (state.appLanguage) {
                            AppLanguage.URDU -> "دوبارہ ٹیسٹ دیں (Retake Test)"
                            AppLanguage.ENGLISH -> "Retake Driving Test 🔄"
                            AppLanguage.ROMAN -> "Dobara Test Dein 🔄"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // SHARE BUTTON
            OutlinedButton(
                onClick = {
                    shareResults(context, state.score, correctCount, totalQuestions, state.appLanguage)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("share_results_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PakGreenPrimary),
                border = ButtonDefaults.outlinedButtonBorder().copy(
                    brush = Brush.horizontalGradient(listOf(PakGreenPrimary, PakGreenDark)),
                    width = 2.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = PakGreenPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (state.appLanguage) {
                            AppLanguage.URDU -> "نتیجہ شیئر کریں (Share Results)"
                            AppLanguage.ENGLISH -> "Share Your Score 📲"
                            AppLanguage.ROMAN -> "Result Share Karein 📲"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // HOME BUTTON
            OutlinedButton(
                onClick = onHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("home_screen_button"),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (state.appLanguage) {
                            AppLanguage.URDU -> "ہوم اسکرین (Home)"
                            AppLanguage.ENGLISH -> "Return to Home 🏠"
                            AppLanguage.ROMAN -> "Home Screen 🏠"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun shareResults(context: Context, score: Int, correct: Int, total: Int, lang: AppLanguage) {
    val shareText = when (lang) {
        AppLanguage.URDU -> """
            🇵🇰 پاکستان ڈرائیونگ لائسنس ٹیسٹ 🚗🛑
            
            میں نے $total میں سے $correct سوالات کے درست جوابات دے کر $score اسکور حاصل کیے! 🏆
            
            کیا آپ پاکستان ٹریفک لائسنس ٹیسٹ پاس کر سکتے ہیں؟ 🚦
        """.trimIndent()
        AppLanguage.ENGLISH -> """
            🇵🇰 Pakistan Driving License Test 🚗🛑
            
            I scored $score by answering $correct out of $total questions correctly! 🏆
            
            Can you pass the Pakistan Traffic Driving Test? 🚦
        """.trimIndent()
        AppLanguage.ROMAN -> """
            🇵🇰 Pakistan Driving License Test 🚗🛑
            
            Main ne $total me se $correct sawal sahi kiye aur $score score banaya! 🏆
            
            Kya aap Pakistan Traffic Test pass kar sakte hain? 🚦
        """.trimIndent()
    }

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "دوستوں کو شیئر کریں")
    context.startActivity(shareIntent)
}
