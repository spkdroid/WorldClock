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

package com.spkd.worldclock.domain.usecase

import com.spkd.worldclock.domain.repository.TimeZoneRepository
import javax.inject.Inject

/**
 * Use case for initializing the application with default time zones.
 * 
 * This use case handles the first-time setup of the application by populating
 * the database with a predefined set of time zones. It ensures that users
 * have a meaningful set of time zones available immediately upon first launch,
 * without requiring manual configuration.
 * 
 * The initialization process is designed to be:
 * - Idempotent: Safe to call multiple times without side effects
 * - Conditional: Only initializes if the database is empty
 * - Comprehensive: Provides global time zone coverage
 * - User-friendly: Includes major cities and business centers
 * 
 * Key characteristics:
 * - One-time operation: Typically runs only during first app launch
 * - Database seeding: Populates empty database with useful defaults
 * - Fail-safe: Handles errors gracefully without breaking app functionality
 * - Performance optimized: Uses batch operations for efficient insertion
 * 
 * The default time zone set typically includes:
 * - Major business centers (New York, London, Tokyo, etc.)
 * - Popular travel destinations
 * - Representative zones from each continent
 * - UTC as a reference point
 * 
 * This use case is typically invoked by:
 * - Application startup sequences
 * - ViewModel initialization in the main activity
 * - Database migration scenarios
 * - Reset/restore functionality
 * 
 * Usage example:
 * ```kotlin
 * viewModelScope.launch {
 *     try {
 *         initializeDefaultTimeZonesUseCase()
 *         // Initialization completed successfully
 *     } catch (e: Exception) {
 *         // Handle initialization error
 *     }
 * }
 * ```
 * 
 * @property repository TimeZoneRepository for data access operations
 * 
 * @see com.spkd.worldclock.domain.repository.TimeZoneRepository
 */
class InitializeDefaultTimeZonesUseCase @Inject constructor(
    private val repository: TimeZoneRepository
) {
    /**
     * Executes the use case to initialize default time zones.
     * 
     * This suspend function performs the initialization operation
     * asynchronously, ensuring it doesn't block the UI thread during
     * the potentially time-consuming database seeding process.
     * 
     * The operation delegates to the repository layer, which contains
     * the actual logic for determining what constitutes "default"
     * time zones and handles the insertion process.
     * 
     * @throws Exception If the initialization process fails due to database
     *                   errors or other system issues
     */
    suspend operator fun invoke() {
        repository.initializeDefaultTimeZones()
    }
}
