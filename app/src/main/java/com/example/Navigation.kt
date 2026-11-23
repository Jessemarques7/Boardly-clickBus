package com.example

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

object Routes {
    const val HOME = "home"
    const val TRIPS = "trips"
    const val ABOUT = "about"
    const val STATUS = "status"
    const val MAP = "map"
    const val VALIDATION = "validation"

    /** Mesma tela de validação, aberta pela aba Check-in (sem botão voltar, com a barra inferior). */
    const val CHECKIN = "checkin"
    const val CONFIRMATION = "confirmation"
}

private data class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(Routes.TRIPS, "Viagens", Icons.Outlined.ConfirmationNumber),
    TopLevelDestination(Routes.HOME, "Início", Icons.Filled.Home),
    TopLevelDestination(Routes.CHECKIN, "Check-in", Icons.Outlined.QrCodeScanner),
    TopLevelDestination(Routes.ABOUT, "Informações", Icons.Outlined.Info)
)

/** Rotas em que a barra inferior fica visível. */
val ROUTES_WITH_BOTTOM_BAR = setOf(Routes.HOME, Routes.TRIPS, Routes.CHECKIN, Routes.ABOUT)

fun NavController.navigateToTopLevel(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun BottomNavBar(currentRoute: String?, onNavigate: (String) -> Unit) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = { onNavigate(destination.route) },
                icon = { Icon(destination.icon, contentDescription = null) },
                label = { Text(destination.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ClickBusPurple,
                    selectedTextColor = ClickBusPurple,
                    indicatorColor = ClickBusPurple.copy(alpha = 0.1f)
                )
            )
        }
    }
}
