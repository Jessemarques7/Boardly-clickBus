package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.theme.MyApplicationTheme

// --- Theme Colors ---
val ClickBusPurple = Color(0xFF6A1B9A)
val CardBackgroundPurple = Color(0xFF7209B7)
val HighlightYellow = Color(0xFFFFCC00)
val StatusGreen = Color(0xFF00C853)
val AppBackground = Color(0xFFF8F9FA)
val TextGray = Color(0xFF5F6368)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AppBackground
                ) {
                    BoardlyApp()
                }
            }
        }
    }
}

@Composable
fun BoardlyApp() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute == "home") {
                BottomNavBar()
            }
        },
        containerColor = AppBackground
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("status") { StatusScreen(navController) }
            composable("map") { MapScreen(navController) }
            composable("validation") { ValidationScreen(navController) }
        }
    }
}

@Composable
fun BottomNavBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Outlined.ConfirmationNumber, contentDescription = "Viagens") },
            label = { Text("Viagens") }
        )
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ClickBusPurple,
                selectedTextColor = ClickBusPurple,
                indicatorColor = ClickBusPurple.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Outlined.QrCodeScanner, contentDescription = "Check-in") },
            label = { Text("Check-in") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Outlined.Person, contentDescription = "Conta") },
            label = { Text("Conta") }
        )
    }
}

// --- Screen 1: Home ---
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ClickBusPurple)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CLICKBUS",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                letterSpacing = 2.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Filled.WorkspacePremium, contentDescription = "Clube", tint = ClickBusPurple, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("clube", color = ClickBusPurple, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        // Promo Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(CardBackgroundPurple, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Chegou pra primeira compra?", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Tem cupom exclusivo!", color = HighlightYellow, fontWeight = FontWeight.Bold)
                }
                Text("R$ 25 OFF", color = HighlightYellow, fontWeight = FontWeight.Black, fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Main Card
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .padding(24.dp)
        ) {
            Column {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column {
                        // Header of the card
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CardBackgroundPurple)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(StatusGreen, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("EMBARQUE ABERTO", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Text("Hoje - 18:00", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }

                        // Content of the card
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("São Paulo", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                    Text("Terminal Tietê", color = TextGray, fontSize = 12.sp)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("01h00", color = ClickBusPurple, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Icon(Icons.Filled.ArrowRightAlt, contentDescription = "Para", tint = ClickBusPurple)
                                    Icon(Icons.Filled.DirectionsBus, contentDescription = "Ônibus", tint = StatusGreen, modifier = Modifier.size(16.dp))
                                }
                                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                                    Text("Rio de", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                    Text("Janeiro", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                    Text("Novo Rio", color = TextGray, fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                InfoItem(label = "Data", value = "15/05/2026")
                                InfoItem(label = "Portão", value = "3")
                                InfoItem(label = "Plataforma", value = "12")
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = { navController.navigate("status") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
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
                                Icon(Icons.Outlined.Info, contentDescription = "Info", tint = HighlightYellow, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Leve seu bilhete e documentos pessoais", color = TextGray, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column {
        Text(label, color = TextGray, fontSize = 12.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ClickBusPurple)
    }
}

// --- Screen 2: Status ---
@Composable
fun StatusScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        // Top App Bar Custom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ClickBusPurple, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .padding(top = 48.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Jornada de embarque", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("São Paulo → Rio de Janeiro", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Stepper
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepItem(title = "Embarque aberto", active = true)
                Divider(modifier = Modifier.weight(1f).padding(horizontal = 8.dp), color = ClickBusPurple.copy(alpha = 0.3f))
                StepItem(title = "No ônibus", active = false)
                Divider(modifier = Modifier.weight(1f).padding(horizontal = 8.dp), color = Color.LightGray)
                StepItem(title = "Em viagem", active = false)
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Circular Indicator
            val infiniteTransition = rememberInfiniteTransition()
            val pulseAnimation by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(220.dp)
            ) {
                CircularProgressIndicator(
                    progress = { 0.75f },
                    modifier = Modifier.fillMaxSize(),
                    color = ClickBusPurple,
                    strokeWidth = 12.dp,
                    trackColor = Color.LightGray.copy(alpha = 0.3f),
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("18:57", fontWeight = FontWeight.Black, fontSize = 48.sp, color = ClickBusPurple)
                    Text("PARA EMBARCAR", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Bus arrived tag
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(StatusGreen.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(StatusGreen, CircleShape)
                        .scale(pulseAnimation)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ônibus chegou ao portão", color = StatusGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Mini card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoItem(label = "SAÍDA", value = "18:57")
                    InfoItem(label = "PLATAFORMA", value = "12")
                    InfoItem(label = "PORTÃO", value = "3")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // Alert box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(AppBackground, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Icon(Icons.Filled.DirectionsBus, contentDescription = "Bus", tint = HighlightYellow)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Dirija-se ao portão 3", fontWeight = FontWeight.Bold, color = ClickBusPurple)
                    Text("Terminal Tietê - Plataforma 12", color = TextGray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate("map") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ClickBusPurple),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Ver Mapa do Terminal", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StepItem(title: String, active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val color = if (active) ClickBusPurple else Color.LightGray
        Text(title, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}


// --- Screen 3: Map ---
@Composable
fun MapScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        // Top App Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ClickBusPurple, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .padding(top = 48.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Jornada de embarque", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Encontre sua plataforma (Terminal Tietê)", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
            }
        }
        
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Mapa do Terminal", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ClickBusPurple)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.ZoomIn, contentDescription = "Zoom", tint = ClickBusPurple, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Zoom", fontSize = 12.sp, color = ClickBusPurple, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                // Map Canvas Area
                val infiniteTransition = rememberInfiniteTransition()
                val pulseAnim by infiniteTransition.animateFloat(
                    initialValue = 10f,
                    targetValue = 20f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 140.dp) // Leave space for bottom card
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height

                        // Draw schematic terminal structures (light lilac/gray)
                        val structureColor = Color(0xFFEDE7F6) // very light purple
                        val wallColor = Color(0xFFD1C4E9)
                        
                        // Basic shapes for corridors/walls
                        drawRoundRect(
                            color = structureColor,
                            topLeft = Offset(canvasWidth * 0.1f, canvasHeight * 0.1f),
                            size = Size(canvasWidth * 0.8f, canvasHeight * 0.8f),
                            cornerRadius = CornerRadius(16f, 16f)
                        )
                        
                        // Block inside
                        drawRoundRect(
                            color = wallColor,
                            topLeft = Offset(canvasWidth * 0.15f, canvasHeight * 0.15f),
                            size = Size(canvasWidth * 0.3f, canvasHeight * 0.3f),
                            cornerRadius = CornerRadius(8f, 8f)
                        )

                        // Path calculation
                        val startPos = Offset(canvasWidth * 0.3f, canvasHeight * 0.8f)
                        val midPos1 = Offset(canvasWidth * 0.6f, canvasHeight * 0.8f)
                        val midPos2 = Offset(canvasWidth * 0.6f, canvasHeight * 0.4f)
                        val endPos = Offset(canvasWidth * 0.8f, canvasHeight * 0.4f)

                        // Draw dashed line
                        val path = Path().apply {
                            moveTo(startPos.x, startPos.y)
                            lineTo(midPos1.x, midPos1.y)
                            lineTo(midPos2.x, midPos2.y)
                            lineTo(endPos.x, endPos.y)
                        }
                        
                        drawPath(
                            path = path,
                            color = ClickBusPurple,
                            style = Stroke(
                                width = 5f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )

                        // Start point with pulse
                        drawCircle(
                            color = ClickBusPurple.copy(alpha = 0.3f),
                            radius = pulseAnim * 1.5f,
                            center = startPos
                        )
                        drawCircle(
                            color = ClickBusPurple,
                            radius = 12f,
                            center = startPos
                        )

                        // End point
                        drawCircle(
                            color = ClickBusPurple.copy(alpha = 0.2f),
                            radius = 40f,
                            center = endPos
                        )
                        drawCircle(
                            color = ClickBusPurple,
                            radius = 16f,
                            center = endPos
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 6f,
                            center = endPos
                        )
                    }
                    
                    // Overlay labels on Canvas
                    Box(modifier = Modifier.fillMaxSize()) {
                        // "Sua Posição" label
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 20.dp, bottom = 50.dp)
                                .background(ClickBusPurple, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Sua Posição", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        // "Sua Plataforma: 12" label
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 90.dp, end = 20.dp)
                                .background(ClickBusPurple, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Sua Plataforma: 12", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LegendItem(color = ClickBusPurple, text = "Sua posição")
                    LegendItem(color = StatusGreen, text = "Plataforma 12")
                    LegendItem(color = ClickBusPurple, text = "Rota")
                }
            }

            // Bottom Card
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .background(AppBackground, CircleShape)
                                        .padding(8.dp)
                                ) {
                                    Icon(Icons.Filled.LocationOn, contentDescription = "Location", tint = ClickBusPurple)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Plataforma 12", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ClickBusPurple)
                                    Text("~3 minutos a pé", color = TextGray, fontSize = 12.sp)
                                }
                            }
                            
                            Box(
                                modifier = Modifier
                                    .background(StatusGreen.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("Baixa ocupação", color = StatusGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { navController.navigate("validation") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ClickBusPurple),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Validar Embarque", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 10.sp, color = TextGray)
    }
}

// --- Screen 4: Validation ---
@Composable
fun ValidationScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        // Top App Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ClickBusPurple, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .padding(top = 48.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Validar embarque", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("SP → RJ - Plataforma 12", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            Spacer(modifier = Modifier.height(16.dp))

            // Main Scanner Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // QR Code Scanner Simulation
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .drawBehind {
                                val strokeWidth = 8f
                                val cornerLength = 40f
                                val color = ClickBusPurple
                                
                                // Top-Left
                                drawLine(color, Offset(0f, 0f), Offset(cornerLength, 0f), strokeWidth)
                                drawLine(color, Offset(0f, 0f), Offset(0f, cornerLength), strokeWidth)
                                // Top-Right
                                drawLine(color, Offset(size.width, 0f), Offset(size.width - cornerLength, 0f), strokeWidth)
                                drawLine(color, Offset(size.width, 0f), Offset(size.width, cornerLength), strokeWidth)
                                // Bottom-Left
                                drawLine(color, Offset(0f, size.height), Offset(cornerLength, size.height), strokeWidth)
                                drawLine(color, Offset(0f, size.height), Offset(0f, size.height - cornerLength), strokeWidth)
                                // Bottom-Right
                                drawLine(color, Offset(size.width, size.height), Offset(size.width - cornerLength, size.height), strokeWidth)
                                drawLine(color, Offset(size.width, size.height), Offset(size.width, size.height - cornerLength), strokeWidth)
                            }
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Fake QR Code
                        Icon(
                            Icons.Filled.QrCode, 
                            contentDescription = "QR Code", 
                            tint = ClickBusPurple, 
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .background(Color.White, CircleShape)
                                .padding(8.dp)
                        ) {
                            Text("CLICK\nBUS", color = ClickBusPurple, fontWeight = FontWeight.Black, fontSize = 10.sp, textAlign = TextAlign.Center)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Box(
                        modifier = Modifier
                            .background(ClickBusPurple.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text("CSG-4567-UVDU-56789", color = ClickBusPurple, fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 1.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Instruction Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Outlined.DocumentScanner, contentDescription = "Scanner", tint = ClickBusPurple)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Aproxime do totem ou mostre\nao motorista", color = TextGray, fontSize = 14.sp, textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            // Divider
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                Text(" OU ", color = TextGray, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp))
                Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Alternative Button
            OutlinedButton(
                onClick = { /* Biometria */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ClickBusPurple),
                border = androidx.compose.foundation.BorderStroke(1.dp, ClickBusPurple)
            ) {
                Icon(Icons.Filled.CameraAlt, contentDescription = "Camera")
                Spacer(modifier = Modifier.width(12.dp))
                Text("Selfie de validação biométrica", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
