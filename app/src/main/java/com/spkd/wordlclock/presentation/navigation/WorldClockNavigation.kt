/*
 * Copyright (C) 2025 SPKD World Clock
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spkd.wordlclock.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spkd.wordlclock.presentation.screen.SplashScreen
import com.spkd.wordlclock.presentation.screen.TimeZoneListScreen
import com.spkd.wordlclock.presentation.screen.WorldClockScreen

/**
 * Main navigation component for the World Clock application.
 * 
 * This Composable function sets up the navigation graph for the entire
 * application using Jetpack Navigation Compose. It defines all the
 * screens and their interconnections, managing the navigation flow
 * throughout the user journey.
 * 
 * Navigation structure:
 * 1. Splash Screen - Initial loading and app initialization
 * 2. World Clock Screen - Main screen displaying selected time zones
 * 3. Time Zone List Screen - Selection interface for time zones
 * 
 * Key features:
 * - Type-safe navigation with Compose Navigation
 * - Proper back stack management
 * - Clear separation of navigation concerns
 * - Centralized navigation logic
 * 
 * Navigation patterns implemented:
 * - Splash → Main (replace, no back navigation)
 * - Main → Selection (standard navigation with back)
 * - Selection → Main (back navigation)
 * 
 * The navigation follows Material Design guidelines for navigation
 * patterns and provides a smooth user experience with appropriate
 * transitions and back stack handling.
 * 
 * @see androidx.navigation.compose.NavHost
 * @see androidx.navigation.compose.rememberNavController
 */
@Composable
fun WorldClockNavigation() {
    // Create and remember the navigation controller for the app
    val navController = rememberNavController()
    
    // Set up the navigation host with defined routes and destinations
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        /**
         * Splash screen destination.
         * 
         * Initial screen shown during app startup. Handles app initialization
         * and automatically navigates to the main screen when ready.
         * Uses replace navigation to prevent back navigation to splash.
         */
        composable("splash") {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate("world_clock") {
                        // Clear splash from back stack to prevent return
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        
        /**
         * Main world clock screen destination.
         * 
         * Primary application screen displaying selected time zones with
         * analog and digital clocks. Provides navigation to time zone
         * selection screen for user customization.
         */
        composable("world_clock") {
            WorldClockScreen(
                onNavigateToTimeZoneList = {
                    navController.navigate("timezone_list")
                }
            )
        }
        
        /**
         * Time zone selection screen destination.
         * 
         * Secondary screen allowing users to select and deselect time zones
         * for display on the main screen. Provides back navigation to return
         * to the main screen after selection changes.
         */
        composable("timezone_list") {
            TimeZoneListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
