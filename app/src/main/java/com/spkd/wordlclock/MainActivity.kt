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

package com.spkd.wordlclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.spkd.wordlclock.presentation.theme.WorldClockTheme
import com.spkd.wordlclock.presentation.navigation.WorldClockNavigation
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity for the World Clock application.
 * 
 * This activity serves as the single entry point for the application and hosts
 * the Jetpack Compose UI. It follows the single-activity architecture pattern
 * recommended for modern Android applications using Compose.
 * 
 * Key features:
 * - Edge-to-edge display support for modern Android devices
 * - Material 3 theming integration
 * - Hilt dependency injection setup
 * - Navigation hosting for app-wide navigation
 * 
 * The @AndroidEntryPoint annotation enables Hilt dependency injection
 * for this activity and allows injection of dependencies throughout
 * the component hierarchy.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    /**
     * Activity lifecycle method called when the activity is being created.
     * 
     * Sets up the UI using Jetpack Compose with the following structure:
     * 1. Enables edge-to-edge display for immersive experience
     * 2. Applies the custom WorldClockTheme
     * 3. Creates a Surface with Material 3 background
     * 4. Hosts the navigation component for app navigation
     * 
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down, this Bundle contains the data it most
     *     recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display for modern Android experience
        enableEdgeToEdge()
        
        // Set up Jetpack Compose UI content
        setContent {
            WorldClockTheme {
                // Create root surface with Material 3 theming
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Host navigation component for app-wide navigation
                    WorldClockNavigation()
                }
            }
        }
    }
}