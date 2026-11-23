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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PANEL_OVERLAP = 32.dp

@Composable
fun HomeScreen(trip: Trip, boarding: BoardingState, onStartJourney: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Fundo roxo do topo; o painel claro abaixo sobe sobre ele com cantos arredondados.
        Column(modifier = Modifier.fillMaxWidth().background(ClickBusPurple)) {
            HomeTopBar()
            PromoBanner()
            Spacer(modifier = Modifier.height(24.dp + PANEL_OVERLAP))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = -PANEL_OVERLAP)
                .background(AppBackground, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .padding(24.dp)
        ) {
            PlatformChangeBanner(boarding, modifier = Modifier.padding(bottom = 16.dp))
            TripCard(trip = trip, boarding = boarding, onStartJourney = onStartJourney)
        }
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("CLICKBUS", color = Color.White, fontWeight = FontWeight.Black, fontSize = 24.sp, letterSpacing = 2.sp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(Icons.Filled.WorkspacePremium, contentDescription = null, tint = ClickBusPurple, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("clube", color = ClickBusPurple, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
private fun PromoBanner() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(CardBackgroundPurple, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Chegou pra primeira compra?", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Tem cupom exclusivo!", color = HighlightYellow, fontWeight = FontWeight.Bold)
        }
        Text("R$ 25 OFF", color = HighlightYellow, fontWeight = FontWeight.Black, fontSize = 20.sp)
    }
}

@Composable
private fun TripCard(trip: Trip, boarding: BoardingState, onStartJourney: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackgroundPurple)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(boarding.phase.color(), CircleShape))
                Spacer(modifier = Modifier.width(8.dp))
                Text(boarding.phase.label.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Text("${trip.dateLabel} · ${trip.departureTime}", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        }

        Column(modifier = Modifier.padding(24.dp)) {
            RouteRow(trip)
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                InfoItem(label = "Saída", value = trip.departureTime)
                InfoItem(label = "Portão", value = boarding.stage.gate)
                InfoItem(label = "Plataforma", value = boarding.stage.platform)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onStartJourney,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ClickBusPurple),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Iniciar jornada de embarque", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Info, contentDescription = null, tint = ClickBusPurple, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Leve seu bilhete e documentos pessoais", color = TextGray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun RouteRow(trip: Trip) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(trip.originCity, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(trip.originTerminal, color = TextGray, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(trip.duration, color = ClickBusPurple, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Icon(Icons.AutoMirrored.Filled.ArrowRightAlt, contentDescription = "Duração da viagem", tint = ClickBusPurple)
            Icon(Icons.Filled.DirectionsBus, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(16.dp))
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            Text(trip.destinationCity, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(trip.destinationTerminal, color = TextGray, fontSize = 12.sp)
        }
    }
}
