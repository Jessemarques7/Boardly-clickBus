package com.example

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val SCANNER_STROKE = 8f
private const val SCANNER_CORNER = 40f

@Composable
fun ValidationScreen(
    trip: Trip,
    boarding: BoardingState,
    onBack: (() -> Unit)?,
    onConfirm: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        ScreenHeader(
            title = "Validar embarque",
            subtitle = "${trip.routeShort} · Plataforma ${boarding.stage.platform}",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TicketCard(ticketCode = trip.ticketCode)
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Outlined.DocumentScanner, contentDescription = null, tint = ClickBusPurple)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Aproxime do totem do portão ${boarding.stage.gate}\nou mostre ao motorista",
                    color = TextGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            if (boarding.phase == BoardingPhase.WAITING_BUS) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "O ônibus ainda não chegou. Você pode validar assim que o embarque abrir.",
                    color = TextGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            SimulationNotice()
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ClickBusPurple),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Simular confirmação de embarque", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun TicketCard(ticketCode: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .drawBehind { drawScannerCorners(ClickBusPurple) }
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.QrCode,
                    contentDescription = "QR Code do bilhete $ticketCode",
                    tint = ClickBusPurple,
                    modifier = Modifier.fillMaxSize()
                )
                Box(modifier = Modifier.background(Color.White, CircleShape).padding(8.dp)) {
                    Text("CLICK\nBUS", color = ClickBusPurple, fontWeight = FontWeight.Black, fontSize = 12.sp, textAlign = TextAlign.Center)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                ticketCode,
                color = ClickBusPurple,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .background(ClickBusPurple.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawScannerCorners(color: Color) {
    val w = size.width
    val h = size.height
    val c = SCANNER_CORNER
    val s = SCANNER_STROKE
    drawLine(color, Offset(0f, 0f), Offset(c, 0f), s)
    drawLine(color, Offset(0f, 0f), Offset(0f, c), s)
    drawLine(color, Offset(w, 0f), Offset(w - c, 0f), s)
    drawLine(color, Offset(w, 0f), Offset(w, c), s)
    drawLine(color, Offset(0f, h), Offset(c, h), s)
    drawLine(color, Offset(0f, h), Offset(0f, h - c), s)
    drawLine(color, Offset(w, h), Offset(w - c, h), s)
    drawLine(color, Offset(w, h), Offset(w, h - c), s)
}
