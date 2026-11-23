package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
/** Largura / altura do desenho do terminal. */
private const val MAP_ASPECT_RATIO = 0.9f

@Composable
fun MapScreen(trip: Trip, boarding: BoardingState, onBack: () -> Unit, onValidate: () -> Unit) {
    var isZoomed by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        ScreenHeader(
            title = "Encontre sua plataforma",
            subtitle = "${trip.originTerminal} · Plataforma ${boarding.stage.platform}",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mapa do terminal", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ClickBusPurple)
                OutlinedButton(onClick = { isZoomed = !isZoomed }) {
                    Icon(
                        if (isZoomed) Icons.Filled.ZoomOut else Icons.Filled.ZoomIn,
                        contentDescription = null,
                        tint = ClickBusPurple,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isZoomed) "Reduzir" else "Ampliar", color = ClickBusPurple)
                }
            }

            PlatformChangeBanner(boarding, modifier = Modifier.padding(top = 8.dp))
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MapCardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().aspectRatio(MAP_ASPECT_RATIO)
            ) {
                TerminalMap(platform = boarding.stage.platform, gate = boarding.stage.gate, isZoomed = isZoomed)
            }
            if (isZoomed) {
                Text(
                    "Arraste para mover o mapa",
                    fontSize = 12.sp,
                    color = TextGray,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                LegendItem(icon = Icons.Filled.LocationOn, text = "Você está aqui")
                LegendItem(icon = Icons.Filled.DirectionsBus, text = "Plataforma ${boarding.stage.platform} (destino)")
            }
            Spacer(modifier = Modifier.height(16.dp))
            MapBottomCard(trip = trip, boarding = boarding, onValidate = onValidate)
        }
    }
}

@Composable
private fun MapBottomCard(trip: Trip, boarding: BoardingState, onValidate: () -> Unit) {
    val busAtGate = boarding.phase != BoardingPhase.WAITING_BUS
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.background(AppBackground, CircleShape).padding(8.dp)) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = ClickBusPurple)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Plataforma ${boarding.stage.platform} · ${boarding.stage.walkToPlatform}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = ClickBusPurple
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.DirectionsBus,
                    contentDescription = null,
                    tint = if (busAtGate) StatusGreen else TextGray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "${if (busAtGate) "Ônibus no portão" else "Ônibus a caminho"} · ${trip.company} · Saída ${trip.departureTime}",
                    color = TextGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onValidate,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ClickBusPurple),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Validar embarque no totem ou portão", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LegendItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = ClickBusPurple, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 12.sp, color = TextGray)
    }
}
