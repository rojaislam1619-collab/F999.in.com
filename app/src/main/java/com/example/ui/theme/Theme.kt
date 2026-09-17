package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = CasinoGold,
    onPrimary = Color.Black,
    primaryContainer = CasinoGoldDark,
    onPrimaryContainer = Color.White,
    secondary = CasinoNeonViolet,
    onSecondary = Color.White,
    secondaryContainer = CasinoPurpleCard,
    onSecondaryContainer = CasinoGoldLight,
    tertiary = CasinoGreen,
    onTertiary = Color.Black,
    background = CasinoBackground,
    onBackground = TextPrimary,
    surface = CasinoPurpleDark,
    onSurface = TextPrimary,
    surfaceVariant = CasinoPurpleSurface,
    onSurfaceVariant = TextSecondary,
    error = CasinoRed,
    onError = Color.White
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = DarkColorScheme, typography = Typography, content = content)
}
