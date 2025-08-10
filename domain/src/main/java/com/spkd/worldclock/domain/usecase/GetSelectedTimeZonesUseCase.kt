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

import com.spkd.worldclock.data.entity.TimeZone
import com.spkd.worldclock.domain.repository.TimeZoneRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving user-selected time zones.
 * 
 * This use case encapsulates the business logic for fetching only the time
 * zones that have been selected by the user for display in the world clock
 * interface. It provides a filtered view of the time zone data, focusing
 * on user preferences.
 * 
 * The use case follows Clean Architecture principles and implements the
 * Command pattern with operator function invocation, enabling it to be
 * called as a function while maintaining dependency injection benefits.
 * 
 * Key characteristics:
 * - Filtered data: Returns only selected time zones
 * - Reactive: Provides Flow for real-time updates when selections change
 * - User-focused: Reflects user preferences and customization
 * - Performance optimized: Reduces data processing for UI components
 * 
 * This use case is typically used by:
 * - Main world clock screen to display selected time zones
 * - Widget providers to show user's preferred time zones
 * - Export/backup functionality for user preferences
 * 
 * Usage example:
 * ```kotlin
 * viewModelScope.launch {
 *     getSelectedTimeZonesUseCase().collect { selectedTimeZones ->
 *         updateClockDisplays(selectedTimeZones)
 *     }
 * }
 * ```
 * 
 * @property repository TimeZoneRepository for data access operations
 * 
 * @see com.spkd.worldclock.domain.repository.TimeZoneRepository
 * @see com.spkd.worldclock.data.entity.TimeZone
 */
class GetSelectedTimeZonesUseCase @Inject constructor(
    private val repository: TimeZoneRepository
) {
    /**
     * Executes the use case to retrieve selected time zones.
     * 
     * This operator function enables the use case to be invoked as a function,
     * providing a clean and intuitive API for the calling code.
     * 
     * The returned Flow emits updates whenever the user's time zone selections
     * change, allowing the UI to react immediately to preference updates.
     * 
     * @return Flow emitting List of user-selected TimeZone entities
     */
    operator fun invoke(): Flow<List<TimeZone>> = repository.getSelectedTimeZones()
}
