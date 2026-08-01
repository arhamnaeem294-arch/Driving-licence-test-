package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = PakGreenContainer,
  onPrimary = PakOnGreenContainer,
  primaryContainer = PakGreenDark,
  onPrimaryContainer = PakGreenLight,
  secondary = PakGoldAccent,
  background = Color(0xFF121D15),
  surface = Color(0xFF18261C),
  onBackground = Color(0xFFE2EFE5),
  onSurface = Color(0xFFE2EFE5)
)

private val LightColorScheme = lightColorScheme(
  primary = PakGreenPrimary,
  onPrimary = Color.White,
  primaryContainer = PakGreenContainer,
  onPrimaryContainer = PakOnGreenContainer,
  secondary = PakGoldAccent,
  background = BackgroundLight,
  surface = SurfaceLight,
  onBackground = TextPrimary,
  onSurface = TextPrimary
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

