package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TripsScreen(trip: Trip, boarding: BoardingState, onTrackBoarding: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        ScreenHeader(title = "Minhas viagens", subtitle = "Próximas viagens")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(trip.routeShort, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ClickBusPurple)
                    Text("${trip.originTerminal} → ${trip.destinationTerminal}", color = TextGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem(label = "Saída", value = "${trip.dateLabel} ${trip.departureTime}")
                        InfoItem(label = "Portão", value = boarding.stage.gate)
                        InfoItem(label = "Plataforma", value = boarding.stage.platform)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onTrackBoarding,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ClickBusPurple),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Acompanhar embarque", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Não há outras viagens nesta demonstração.",
                color = TextGray,
                fontSize = 14.sp
            )
        }
    }
}
