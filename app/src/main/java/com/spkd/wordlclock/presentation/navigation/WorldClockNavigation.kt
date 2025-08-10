package com.spkd.wordlclock.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spkd.wordlclock.presentation.screen.SplashScreen
import com.spkd.wordlclock.presentation.screen.TimeZoneListScreen
import com.spkd.wordlclock.presentation.screen.WorldClockScreen

@Composable
fun WorldClockNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate("world_clock") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("world_clock") {
            WorldClockScreen(
                onNavigateToTimeZoneList = {
                    navController.navigate("timezone_list")
                }
            )
        }
        composable("timezone_list") {
            TimeZoneListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
