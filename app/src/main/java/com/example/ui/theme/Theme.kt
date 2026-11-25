package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * O protótipo foi desenhado só em modo claro e usa cores fixas nas telas.
 * Por isso o tema não segue o modo escuro nem as cores dinâmicas do Android 12+:
 * isso deixaria textos claros sobre os cartões brancos.
 */
private val BoardlyColorScheme =
  lightColorScheme(
    primary = Color(0xFF6A1B9A),
    onPrimary = Color.White,
    secondary = Color(0xFF7209B7),
    onSecondary = Color.White,
    tertiary = Color(0xFFFFCC00),
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF1C1B1F),
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
  )

@Composable
fun BoardlyTheme(content: @Composable () -> Unit) {
  MaterialTheme(colorScheme = BoardlyColorScheme, typography = Typography, content = content)
}
