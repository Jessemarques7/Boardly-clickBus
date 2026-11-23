package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DEMO_FLOW = listOf(
    "Início: veja a viagem e toque em \"Iniciar jornada de embarque\".",
    "Status: acompanhe as atualizações do terminal. Elas chegam a cada 10 s, ou toque em \"Avançar simulação do terminal\".",
    "No segundo evento a plataforma muda de 12 para 14, e todas as telas avisam.",
    "Mapa: veja a rota até a plataforma e use \"Ampliar\" para aproximar.",
    "Validação: mostre o bilhete no totem ou ao motorista.",
    "Confirmação: simule o embarque e volte ao início."
)

private val SIMULATED_ITEMS = listOf(
    "Dados da viagem são fixos.",
    "Os eventos do terminal (chegada do ônibus, troca de plataforma, abertura do embarque) seguem um roteiro simulado.",
    "O mapa do terminal é ilustrativo e não usa GPS.",
    "A validação do bilhete é simulada; não há integração com a ClickBus nem com o terminal.",
    "O app não usa câmera nem biometria."
)

@Composable
fun AboutScreen() {
    Column(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        ScreenHeader(title = "Sobre o Boardly", subtitle = "Protótipo — Challenge ClickBus 2026")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                "O Boardly orienta o passageiro do bilhete até o portão de embarque.",
                fontSize = 16.sp,
                color = ClickBusPurple,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            BulletCard(title = "Como demonstrar", items = DEMO_FLOW)
            Spacer(modifier = Modifier.height(16.dp))
            BulletCard(title = "O que é simulado", items = SIMULATED_ITEMS)
        }
    }
}

@Composable
private fun BulletCard(title: String, items: List<String>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ClickBusPurple)
            Spacer(modifier = Modifier.height(8.dp))
            items.forEach { item ->
                Text("• $item", color = TextGray, fontSize = 14.sp, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}
