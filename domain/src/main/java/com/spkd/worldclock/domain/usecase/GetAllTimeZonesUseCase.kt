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
 * Use case for retrieving all available time zones.
 * 
 * This use case encapsulates the business logic for fetching the complete
 * list of time zones available in the application. It follows the Clean
 * Architecture principles by providing a clear separation between the
 * presentation layer and data access layer.
 * 
 * The use case implements the Command pattern with operator function
 * invocation, making it callable as a function while maintaining the
 * benefits of dependency injection and testability.
 * 
 * Key characteristics:
 * - Single responsibility: Only handles retrieval of all time zones
 * - Reactive: Returns Flow for real-time data updates
 * - Testable: Dependencies are injected, enabling easy mocking
 * - Reusable: Can be used across multiple presentation layer components
 * 
 * Usage example:
 * ```kotlin
 * viewModelScope.launch {
 *     getAllTimeZonesUseCase().collect { timeZones ->
 *         // Handle time zones list
 *     }
 * }
 * ```
 * 
 * @property repository TimeZoneRepository for data access operations
 * 
 * @see com.spkd.worldclock.domain.repository.TimeZoneRepository
 * @see com.spkd.worldclock.data.entity.TimeZone
 */
class GetAllTimeZonesUseCase @Inject constructor(
    private val repository: TimeZoneRepository
) {
    /**
     * Executes the use case to retrieve all time zones.
     * 
     * This operator function enables the use case to be invoked as a function,
     * providing a clean and intuitive API for the calling code.
     * 
     * The returned Flow allows the presentation layer to observe changes
     * in the time zone data and react accordingly with UI updates.
     * 
     * @return Flow emitting List of all TimeZone entities from the repository
     */
    operator fun invoke(): Flow<List<TimeZone>> = repository.getAllTimeZones()
}
