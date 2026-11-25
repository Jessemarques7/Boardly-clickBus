package com.example

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmationScreen(
    trip: Trip,
    boarding: BoardingState,
    onRepeat: () -> Unit,
    onBackToHome: () -> Unit
) {
    // O voltar do sistema equivale a "Repetir validação": desfaz a confirmação simulada.
    BackHandler(onBack = onRepeat)

    Column(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        ScreenHeader(title = "Embarque confirmado", subtitle = "${trip.routeShort} · Plataforma ${boarding.stage.platform}")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(StatusGreen.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = StatusGreen,
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Tudo certo! Boa viagem.",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = ClickBusPurple,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Seu embarque foi registrado. Siga até o ônibus na plataforma ${boarding.stage.platform}.",
                color = TextGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    InfoItem(label = "BILHETE", value = trip.ticketCode)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem(label = "SAÍDA", value = trip.departureTime)
                        InfoItem(label = "PLATAFORMA", value = boarding.stage.platform)
                        InfoItem(label = "PORTÃO", value = boarding.stage.gate)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            SimulationNotice()

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onBackToHome,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ClickBusPurple),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Voltar ao início", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onRepeat,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ClickBusPurple),
                border = BorderStroke(1.dp, ClickBusPurple)
            ) {
                Text("Repetir validação", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/** Aviso de que a tela mostra uma simulação, não uma validação real. */
@Composable
fun SimulationNotice() {
    Text(
        "Demonstração: nenhum bilhete real foi validado.",
        color = TextGray,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(HighlightYellow.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    )
}
