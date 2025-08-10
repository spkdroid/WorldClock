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

package com.spkd.worldclock.core.database

import androidx.room.*
import com.spkd.worldclock.data.entity.TimeZone
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for TimeZone entity operations.
 * 
 * This interface defines the contract for all database operations related to
 * time zones. It uses Room persistence library annotations to generate the
 * implementation at compile time, providing type-safe database access.
 * 
 * The DAO follows reactive programming patterns by returning Flow objects
 * for queries that should be observed for changes, and suspend functions
 * for one-time operations.
 * 
 * Key features:
 * - Reactive queries using Kotlin Flow for real-time updates
 * - Suspend functions for asynchronous database operations
 * - Conflict resolution strategies for data integrity
 * - Optimized queries with proper indexing
 * 
 * @see androidx.room.Dao
 * @see com.spkd.worldclock.data.entity.TimeZone
 */
@Dao
interface TimeZoneDao {
    
    /**
     * Retrieves all time zones from the database, ordered alphabetically.
     * 
     * Returns a Flow that emits the complete list of time zones whenever
     * the underlying data changes. This enables reactive UI updates.
     * 
     * @return Flow emitting List of all TimeZone entities ordered by display name
     */
    @Query("SELECT * FROM timezone_table ORDER BY display_name ASC")
    fun getAllTimeZones(): Flow<List<TimeZone>>

    /**
     * Retrieves all user-selected time zones from the database.
     * 
     * Returns a Flow that emits only the time zones marked as selected
     * by the user, ordered alphabetically for consistent display.
     * 
     * @return Flow emitting List of selected TimeZone entities
     */
    @Query("SELECT * FROM timezone_table WHERE is_selected = 1 ORDER BY display_name ASC")
    fun getSelectedTimeZones(): Flow<List<TimeZone>>

    /**
     * Retrieves a specific time zone by its unique identifier.
     * 
     * This is a one-time query operation for fetching individual time zones.
     * Returns null if no time zone with the specified UID exists.
     * 
     * @param uid Unique identifier of the time zone to retrieve
     * @return TimeZone entity if found, null otherwise
     */
    @Query("SELECT * FROM timezone_table WHERE uid = :uid")
    suspend fun getTimeZoneById(uid: String): TimeZone?

    /**
     * Inserts a single time zone into the database.
     * 
     * Uses REPLACE conflict resolution strategy to handle duplicate UIDs
     * by replacing existing entries with the new data.
     * 
     * @param timeZone TimeZone entity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeZone(timeZone: TimeZone)

    /**
     * Inserts multiple time zones into the database in a single transaction.
     * 
     * This batch operation is more efficient than individual inserts for
     * large datasets. Uses REPLACE strategy for conflict resolution.
     * 
     * @param timeZones List of TimeZone entities to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeZones(timeZones: List<TimeZone>)

    /**
     * Updates an existing time zone in the database.
     * 
     * The update is based on the primary key (uid) of the entity.
     * All fields of the entity will be updated with the new values.
     * 
     * @param timeZone TimeZone entity with updated values
     */
    @Update
    suspend fun updateTimeZone(timeZone: TimeZone)

    /**
     * Deletes a specific time zone from the database.
     * 
     * The deletion is based on the primary key (uid) of the entity.
     * 
     * @param timeZone TimeZone entity to delete
     */
    @Delete
    suspend fun deleteTimeZone(timeZone: TimeZone)

    /**
     * Deletes all time zones from the database.
     * 
     * This operation clears the entire timezone_table. Use with caution
     * as this operation is irreversible.
     */
    @Query("DELETE FROM timezone_table")
    suspend fun deleteAllTimeZones()

    /**
     * Updates the selection status of a specific time zone.
     * 
     * This optimized update operation only modifies the is_selected field
     * without requiring a full entity update, improving performance.
     * 
     * @param uid Unique identifier of the time zone to update
     * @param isSelected New selection status (true for selected, false for unselected)
     */
    @Query("UPDATE timezone_table SET is_selected = :isSelected WHERE uid = :uid")
    suspend fun updateTimeZoneSelection(uid: String, isSelected: Boolean)
    
    /**
     * Retrieves the total count of time zones in the database.
     * 
     * This utility method is useful for determining if the database has been
     * initialized or for pagination calculations.
     * 
     * @return Total number of time zone records in the database
     */
    @Query("SELECT COUNT(*) FROM timezone_table")
    suspend fun getTimeZoneCount(): Int
}