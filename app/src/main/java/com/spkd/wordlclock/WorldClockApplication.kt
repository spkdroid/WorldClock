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

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class for the World Clock application.
 * 
 * This class serves as the application-level entry point and is responsible for
 * initializing Hilt dependency injection framework. The @HiltAndroidApp annotation
 * triggers Hilt's code generation and sets up the application-level dependency
 * injection container.
 * 
 * Key responsibilities:
 * - Initialize Hilt dependency injection
 * - Serve as the root of the dependency injection graph
 * - Provide application context for dependency injection
 * 
 * The HiltAndroidApp annotation generates the necessary components and modules
 * for dependency injection throughout the application lifecycle.
 */
@HiltAndroidApp
class WorldClockApplication : Application()
