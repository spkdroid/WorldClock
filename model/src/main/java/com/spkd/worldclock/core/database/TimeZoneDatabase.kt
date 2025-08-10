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

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.spkd.worldclock.data.entity.TimeZone

/**
 * Room database class for the World Clock application.
 * 
 * This abstract class serves as the main database configuration and provides
 * access to the DAOs. Room uses this class to generate the implementation
 * at compile time, creating the SQLite database structure and providing
 * type-safe database access.
 * 
 * Database configuration:
 * - Version: 1 (initial version)
 * - Export schema: false (for simplicity in this version)
 * - Entities: TimeZone
 * 
 * The database follows the singleton pattern to ensure a single instance
 * throughout the application lifecycle, managed by the dependency injection
 * framework.
 * 
 * Key features:
 * - Room persistence library integration
 * - Type-safe database access
 * - Automatic SQL generation from annotations
 * - Transaction support and thread safety
 * 
 * @see androidx.room.Database
 * @see androidx.room.RoomDatabase
 * @see com.spkd.worldclock.data.entity.TimeZone
 * @see com.spkd.worldclock.core.database.TimeZoneDao
 */
@Database(
    entities = [TimeZone::class],
    version = 1,
    exportSchema = false
)
abstract class TimeZoneDatabase : RoomDatabase() {
    
    /**
     * Provides access to TimeZone data access object.
     * 
     * Room automatically generates the implementation of this DAO interface
     * at compile time, providing all the database operations defined in
     * the TimeZoneDao interface.
     * 
     * @return TimeZoneDao instance for database operations
     */
    abstract fun timeZoneDao(): TimeZoneDao

    companion object {
        /**
         * Database name used for SQLite file creation.
         * 
         * This constant ensures consistent database naming across the application
         * and is used by the dependency injection module for database creation.
         */
        const val DATABASE_NAME = "timezone_database"
    }
}