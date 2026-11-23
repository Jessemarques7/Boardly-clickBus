package com.example

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Fases mostradas no indicador de etapas, em ordem. */
private val STEPPER_PHASES = BoardingPhase.entries

@Composable
fun StatusScreen(
    trip: Trip,
    boarding: BoardingState,
    onBack: () -> Unit,
    onOpenMap: () -> Unit,
    onAdvanceSimulation: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        ScreenHeader(title = "Jornada de embarque", subtitle = trip.routeShort, onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PhaseStepper(current = boarding.phase)
            Spacer(modifier = Modifier.height(32.dp))
            DepartureCountdown(boarding = boarding, departureTime = trip.departureTime)
            Spacer(modifier = Modifier.height(24.dp))
            PhaseBadge(phase = boarding.phase)
            Spacer(modifier = Modifier.height(24.dp))
            PlatformChangeBanner(boarding, modifier = Modifier.padding(bottom = 16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoItem(label = "SAÍDA", value = trip.departureTime)
                    InfoItem(label = "PLATAFORMA", value = boarding.stage.platform)
                    InfoItem(label = "PORTÃO", value = boarding.stage.gate)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            DirectionsCard(trip = trip, stage = boarding.stage)
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onOpenMap,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ClickBusPurple),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Ver mapa do terminal", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            if (boarding.canAdvance) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onAdvanceSimulation) {
                    Text("Avançar simulação do terminal", color = ClickBusPurple)
                }
            }
        }
    }
}

@Composable
private fun PhaseStepper(current: BoardingPhase) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        STEPPER_PHASES.forEachIndexed { index, phase ->
            val reached = phase.ordinal <= current.ordinal
            Text(
                phase.shortLabel,
                color = if (reached) ClickBusPurple else TextGray,
                fontSize = 12.sp,
                fontWeight = if (phase == current) FontWeight.Black else FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            if (index < STEPPER_PHASES.lastIndex) {
                val nextReached = STEPPER_PHASES[index + 1].ordinal <= current.ordinal
                HorizontalDivider(
                    modifier = Modifier.weight(1f).padding(horizontal = 6.dp),
                    color = if (nextReached) ClickBusPurple else Color.LightGray
                )
            }
        }
    }
}

@Composable
private fun DepartureCountdown(boarding: BoardingState, departureTime: String) {
    val progress by animateFloatAsState(targetValue = boarding.progress, label = "journeyProgress")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(200.dp)
            .semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Polite }
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            color = ClickBusPurple,
            strokeWidth = 12.dp,
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (boarding.isBoarded) {
                Text("Boa viagem!", fontWeight = FontWeight.Black, fontSize = 26.sp, color = ClickBusPurple)
            } else {
                Text("${boarding.stage.minutesToDeparture} min", fontWeight = FontWeight.Black, fontSize = 40.sp, color = ClickBusPurple)
                Text("PARA A SAÍDA", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Text("Saída às $departureTime", color = TextGray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun PhaseBadge(phase: BoardingPhase) {
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing), RepeatMode.Reverse),
        label = "pulseScale"
    )
    val color = phase.color()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(modifier = Modifier.size(10.dp).scale(pulse).background(color, CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Text(phase.label, color = TextGray, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
private fun DirectionsCard(trip: Trip, stage: BoardingStage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.background(AppBackground, RoundedCornerShape(8.dp)).padding(8.dp)) {
            Icon(Icons.Filled.DirectionsBus, contentDescription = null, tint = ClickBusPurple)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text("Dirija-se ao portão ${stage.gate}", fontWeight = FontWeight.Bold, color = ClickBusPurple)
            Text("${trip.originTerminal} · Plataforma ${stage.platform} · ${stage.walkToPlatform}", color = TextGray, fontSize = 12.sp)
        }
    }
}
