package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Laranja escuro: mantém contraste de texto sobre o fundo claro do alerta. */
val AlertOrange = Color(0xFFBF360C)

@Composable
fun InfoItem(label: String, value: String) {
    Column {
        Text(label, color = TextGray, fontSize = 12.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ClickBusPurple)
    }
}

/** Alerta exibido quando o terminal troca a plataforma. Mostra nada se não houve troca. */
@Composable
fun PlatformChangeBanner(state: BoardingState, modifier: Modifier = Modifier) {
    val previous = state.previousPlatform ?: return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(AlertOrange.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .padding(12.dp)
            .semantics { liveRegion = LiveRegionMode.Polite }
    ) {
        Icon(Icons.Filled.Warning, contentDescription = null, tint = AlertOrange, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                "Plataforma alterada: $previous → ${state.stage.platform}",
                color = AlertOrange,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                "Siga para o portão ${state.stage.gate}.",
                color = TextGray,
                fontSize = 12.sp
            )
        }
    }
}

/** Cor do status conforme a fase: verde quando o passageiro já pode agir. */
fun BoardingPhase.color(): Color = when (this) {
    BoardingPhase.WAITING_BUS -> HighlightYellow
    BoardingPhase.BUS_AT_GATE -> StatusGreen
    BoardingPhase.BOARDING_OPEN -> StatusGreen
    BoardingPhase.BOARDED -> StatusGreen
}
