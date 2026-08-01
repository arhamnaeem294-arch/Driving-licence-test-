package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.ui.theme.PakGoldAccent
import com.example.ui.theme.PakGreenDark
import com.example.ui.theme.PakGreenPrimary
import com.example.viewmodel.QuizMode
import com.example.viewmodel.QuizUiState

@Composable
fun StartScreen(
    state: QuizUiState,
    onStartGame: () -> Unit,
    onSelectMode: (QuizMode) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleSound: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Sound & Top Settings Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Country Tag
                Surface(
                    color = PakGreenPrimary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🇵🇰", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (state.appLanguage) {
                                AppLanguage.URDU -> "پاکستان ٹریفک سائنس"
                                AppLanguage.ENGLISH -> "Pak Traffic License"
                                AppLanguage.ROMAN -> "Pak Traffic Signs"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PakGreenPrimary
                        )
                    }
                }

                // Sound Toggle Button
                IconButton(
                    onClick = onToggleSound,
                    modifier = Modifier
                        .testTag("toggle_sound_button")
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = if (state.isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Sound Toggle",
                        tint = if (state.isSoundEnabled) PakGreenPrimary else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // LANGUAGE SELECTOR CARD (Urdu | English | Roman Urdu)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("language_selector_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (state.appLanguage) {
                            AppLanguage.URDU -> "🌐 زبان منتخب کریں (Select Language):"
                            AppLanguage.ENGLISH -> "🌐 Choose App Language:"
                            AppLanguage.ROMAN -> "🌐 Zaban Chunein (Select Language):"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AppLanguage.values().forEach { lang ->
                            val isSelected = state.appLanguage == lang
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onSelectLanguage(lang) }
                                    .testTag("lang_button_${lang.code}"),
                                color = if (isSelected) PakGreenPrimary else Color.White,
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                shape = RoundedCornerShape(12.dp),
                                shadowElevation = if (isSelected) 4.dp else 1.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = lang.flagEmoji, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = lang.titleEnglish,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hero Header Card (Pakistani Flag Theme)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(PakGreenPrimary, PakGreenDark)
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🛑 🚦 🛣️",
                            fontSize = 36.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when (state.appLanguage) {
                                AppLanguage.URDU -> "پاکستان ڈرائیونگ لائسنس"
                                AppLanguage.ENGLISH -> "Pakistan Driving License"
                                AppLanguage.ROMAN -> "Pakistan Driving License"
                            },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = when (state.appLanguage) {
                                AppLanguage.URDU -> "ٹیسٹ (Exam Prep)"
                                AppLanguage.ENGLISH -> "Exam Test 2026"
                                AppLanguage.ROMAN -> "Test (Driving Test)"
                            },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = PakGoldAccent,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "50 Traffic Signs & Rules Master",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // High Score Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("high_score_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PakGoldAccent, PakGreenPrimary)))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "High Score",
                            tint = PakGoldAccent,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Best Score",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = when (state.appLanguage) {
                                    AppLanguage.URDU -> "اعلیٰ ترین اسکور"
                                    AppLanguage.ENGLISH -> "High Record"
                                    AppLanguage.ROMAN -> "Aala Score"
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Text(
                        text = "${state.highScore}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = PakGreenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Test Mode Selection Header
            Text(
                text = when (state.appLanguage) {
                    AppLanguage.URDU -> "ٹیسٹ کا زمرہ یا گاڑی کی قسم منتخب کریں:"
                    AppLanguage.ENGLISH -> "Select Test Category or Vehicle Type:"
                    AppLanguage.ROMAN -> "Test ki category ya gaari chunein:"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            QuizMode.values().forEach { mode ->
                val isSelected = state.selectedMode == mode
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onSelectMode(mode) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) PakGreenPrimary.copy(alpha = 0.12f) else Color.White
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            if (isSelected) listOf(PakGreenPrimary, PakGreenPrimary) else listOf(Color(0xFFCBD5E1), Color(0xFFCBD5E1))
                        ),
                        width = if (isSelected) 2.dp else 1.dp
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (isSelected) "🟢" else mode.iconEmoji,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = mode.getLabel(state.appLanguage),
                                fontSize = 16.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                color = if (isSelected) PakGreenPrimary else Color(0xFF1E293B)
                            )
                        }
                        if (isSelected) {
                            Surface(
                                color = PakGreenPrimary,
                                shape = RoundedCornerShape(20)
                            ) {
                                Text(
                                    text = when (state.appLanguage) {
                                        AppLanguage.URDU -> "منتخب"
                                        AppLanguage.ENGLISH -> "Selected"
                                        AppLanguage.ROMAN -> "Chuna Gaya"
                                    },
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Start Test Primary Button
            Button(
                onClick = onStartGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .testTag("start_test_button")
                    .shadow(6.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PakGreenPrimary,
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Test",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (state.appLanguage) {
                            AppLanguage.URDU -> "ٹیسٹ شروع کریں (Start Test)"
                            AppLanguage.ENGLISH -> "Start Driving Test 🚀"
                            AppLanguage.ROMAN -> "Test Shuru Karein 🚀"
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Rules Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = when (state.appLanguage) {
                            AppLanguage.URDU -> "ٹیسٹ کی اہم ہدایات (Instructions):"
                            AppLanguage.ENGLISH -> "Important Test Rules & Scoring:"
                            AppLanguage.ROMAN -> "Test Hidayat (Instructions):"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PakGreenPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = when (state.appLanguage) {
                            AppLanguage.URDU -> "• صحیح جواب: +10 اسکور 🟢\n• غلط جواب: 0 اسکور (کوئی کٹوتی نہیں) 🔴\n• 5 سوالات کے بعد ایڈ وقفہ 📺\n• پورا ٹیسٹ مکمل ہونے پر (پاس / اوسط / ناکام) کا نتیجہ دیا جائے گا۔"
                            AppLanguage.ENGLISH -> "• Correct Answer: +10 Score 🟢\n• Incorrect Answer: 0 Score (No Penalty) 🔴\n• Sponsored Break after every 5 questions 📺\n• Final Pass / Average / Fail result summary at completion."
                            AppLanguage.ROMAN -> "• Sahi Jawab: +10 Score 🟢\n• Galat Jawab: 0 Score 🔴\n• 5 sawal ke baad Ad break 📺\n• Poora test complete hone par result milega."
                        },
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
