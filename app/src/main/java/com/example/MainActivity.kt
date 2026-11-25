package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.theme.BoardlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Cabeçalhos roxos ficam atrás da barra de status, então os ícones dela são claros.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            BoardlyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = AppBackground) {
                    BoardlyApp()
                }
            }
        }
    }
}

@Composable
fun BoardlyApp(
    trip: Trip = DEMO_TRIP,
    boardingViewModel: BoardingViewModel = viewModel()
) {
    val navController = rememberNavController()
    val boarding by boardingViewModel.state.collectAsStateWithLifecycle()
    val goBack: () -> Unit = { navController.popBackStack() }
    val confirmBoarding: () -> Unit = {
        boardingViewModel.confirmBoarding()
        navController.navigate(Routes.CONFIRMATION)
    }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute in ROUTES_WITH_BOTTOM_BAR) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route -> navController.navigateToTopLevel(route) }
                )
            }
        },
        containerColor = AppBackground,
        // Cada tela trata a barra de status; a barra de navegação é tratada no NavHost.
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .navigationBarsPadding()
        ) {
            composable(Routes.HOME) {
                HomeScreen(trip, boarding, onStartJourney = { navController.navigate(Routes.STATUS) })
            }
            composable(Routes.TRIPS) {
                TripsScreen(trip, boarding, onTrackBoarding = { navController.navigate(Routes.STATUS) })
            }
            composable(Routes.ABOUT) { AboutScreen() }
            composable(Routes.STATUS) {
                StatusScreen(
                    trip = trip,
                    boarding = boarding,
                    onBack = goBack,
                    onOpenMap = { navController.navigate(Routes.MAP) },
                    onAdvanceSimulation = boardingViewModel::advance
                )
            }
            composable(Routes.MAP) {
                MapScreen(trip, boarding, onBack = goBack, onValidate = { navController.navigate(Routes.VALIDATION) })
            }
            composable(Routes.VALIDATION) {
                ValidationScreen(trip, boarding, onBack = goBack, onConfirm = confirmBoarding)
            }
            composable(Routes.CHECKIN) {
                ValidationScreen(trip, boarding, onBack = null, onConfirm = confirmBoarding)
            }
            composable(Routes.CONFIRMATION) {
                ConfirmationScreen(
                    trip = trip,
                    boarding = boarding,
                    onRepeat = {
                        boardingViewModel.repeatValidation()
                        navController.popBackStack()
                    },
                    onBackToHome = {
                        boardingViewModel.restart()
                        navController.popBackStack(Routes.HOME, inclusive = false)
                    }
                )
            }
        }
    }
}
