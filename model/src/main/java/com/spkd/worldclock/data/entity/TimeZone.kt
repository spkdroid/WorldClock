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

package com.spkd.worldclock.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Data entity representing a time zone in the application.
 * 
 * This class serves as both a Room database entity and a data transfer object
 * throughout the application layers. It encapsulates all information needed
 * to represent and manage time zones within the application.
 * 
 * The entity uses Room annotations for database persistence and implements
 * Parcelable for efficient data transfer between Android components.
 * 
 * Database mapping:
 * - Table name: "timezone_table"
 * - Primary key: uid (unique identifier)
 * - Indexed columns: timezone, is_selected (for query optimization)
 * 
 * @property uid Unique identifier for the time zone (primary key)
 * @property timeZoneName Standard time zone identifier (e.g., "America/New_York")
 * @property displayName User-friendly name for display purposes
 * @property isSelected Whether this time zone is currently selected by the user
 * 
 * @see androidx.room.Entity
 * @see android.os.Parcelable
 */
@Entity(tableName = "timezone_table")
@Parcelize
data class TimeZone(
    @PrimaryKey 
    val uid: String,
    
    @ColumnInfo(name = "timezone") 
    val timeZoneName: String,
    
    @ColumnInfo(name = "display_name") 
    val displayName: String = "",
    
    @ColumnInfo(name = "is_selected") 
    val isSelected: Boolean = false
) : Parcelable