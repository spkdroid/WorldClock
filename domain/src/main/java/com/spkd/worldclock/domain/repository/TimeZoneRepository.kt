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

package com.spkd.worldclock.domain.repository

import com.spkd.worldclock.data.entity.TimeZone
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for time zone data operations.
 * 
 * This interface defines the contract for data access operations related to
 * time zones, following the Repository pattern. It abstracts the data layer
 * implementation details from the domain layer, enabling clean separation
 * of concerns and testability.
 * 
 * The repository interface serves as:
 * - A contract between domain and data layers
 * - An abstraction for different data sources (local database, remote APIs)
 * - A foundation for dependency injection and testing
 * - A boundary for reactive programming with Kotlin Flow
 * 
 * Implementation considerations:
 * - All operations are designed to be thread-safe
 * - Reactive queries return Flow for real-time updates
 * - Suspend functions enable asynchronous, non-blocking operations
 * - Error handling is delegated to the implementation layer
 * 
 * @see com.spkd.worldclock.data.entity.TimeZone
 * @see kotlinx.coroutines.flow.Flow
 */
interface TimeZoneRepository {
    
    /**
     * Retrieves all available time zones from the data source.
     * 
     * Returns a Flow that emits the complete list of time zones, enabling
     * reactive UI updates when the underlying data changes.
     * 
     * @return Flow emitting List of all TimeZone entities
     */
    fun getAllTimeZones(): Flow<List<TimeZone>>
    
    /**
     * Retrieves all user-selected time zones from the data source.
     * 
     * Returns a Flow that emits only time zones marked as selected,
     * filtered and ready for display in the main UI.
     * 
     * @return Flow emitting List of selected TimeZone entities
     */
    fun getSelectedTimeZones(): Flow<List<TimeZone>>
    
    /**
     * Retrieves a specific time zone by its unique identifier.
     * 
     * This is a one-time operation for fetching individual time zone data.
     * Returns null if no time zone with the specified UID exists.
     * 
     * @param uid Unique identifier of the time zone
     * @return TimeZone entity if found, null otherwise
     */
    suspend fun getTimeZoneById(uid: String): TimeZone?
    
    /**
     * Inserts a single time zone into the data source.
     * 
     * This operation adds a new time zone or updates an existing one
     * based on the conflict resolution strategy of the implementation.
     * 
     * @param timeZone TimeZone entity to insert
     */
    suspend fun insertTimeZone(timeZone: TimeZone)
    
    /**
     * Inserts multiple time zones into the data source.
     * 
     * This batch operation is optimized for bulk data insertion,
     * typically used during application initialization or data migration.
     * 
     * @param timeZones List of TimeZone entities to insert
     */
    suspend fun insertTimeZones(timeZones: List<TimeZone>)
    
    /**
     * Updates an existing time zone in the data source.
     * 
     * The update operation modifies all fields of the specified time zone
     * based on the primary key (uid).
     * 
     * @param timeZone TimeZone entity with updated values
     */
    suspend fun updateTimeZone(timeZone: TimeZone)
    
    /**
     * Deletes a specific time zone from the data source.
     * 
     * Removes the time zone identified by the entity's primary key (uid).
     * 
     * @param timeZone TimeZone entity to delete
     */
    suspend fun deleteTimeZone(timeZone: TimeZone)
    
    /**
     * Deletes all time zones from the data source.
     * 
     * This operation clears the entire time zone dataset. Use with caution
     * as this operation may be irreversible depending on the implementation.
     */
    suspend fun deleteAllTimeZones()
    
    /**
     * Updates the selection status of a specific time zone.
     * 
     * This optimized operation modifies only the selection flag without
     * requiring a full entity update, improving performance for frequent
     * selection/deselection operations.
     * 
     * @param uid Unique identifier of the time zone to update
     * @param isSelected New selection status (true for selected, false for unselected)
     */
    suspend fun updateTimeZoneSelection(uid: String, isSelected: Boolean)
    
    /**
     * Retrieves the total count of time zones in the data source.
     * 
     * This utility method provides metadata about the dataset size,
     * useful for initialization checks and pagination calculations.
     * 
     * @return Total number of time zone records
     */
    suspend fun getTimeZoneCount(): Int
    
    /**
     * Initializes the data source with default time zones.
     * 
     * This method sets up the application with a predefined set of time zones
     * when the database is empty or during first launch. The implementation
     * defines which time zones are considered "default".
     */
    suspend fun initializeDefaultTimeZones()
}
