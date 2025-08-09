package com.spkd.worldclock.core.database

import androidx.room.*
import com.spkd.worldclock.data.entity.TimeZone
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeZoneDao {
    @Query("SELECT * FROM timezone_table ORDER BY display_name ASC")
    fun getAllTimeZones(): Flow<List<TimeZone>>

    @Query("SELECT * FROM timezone_table WHERE is_selected = 1 ORDER BY display_name ASC")
    fun getSelectedTimeZones(): Flow<List<TimeZone>>

    @Query("SELECT * FROM timezone_table WHERE uid = :uid")
    suspend fun getTimeZoneById(uid: String): TimeZone?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeZone(timeZone: TimeZone)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeZones(timeZones: List<TimeZone>)

    @Update
    suspend fun updateTimeZone(timeZone: TimeZone)

    @Delete
    suspend fun deleteTimeZone(timeZone: TimeZone)

    @Query("DELETE FROM timezone_table")
    suspend fun deleteAllTimeZones()

    @Query("UPDATE timezone_table SET is_selected = :isSelected WHERE uid = :uid")
    suspend fun updateTimeZoneSelection(uid: String, isSelected: Boolean)
    
    @Query("SELECT COUNT(*) FROM timezone_table")
    suspend fun getTimeZoneCount(): Int
}