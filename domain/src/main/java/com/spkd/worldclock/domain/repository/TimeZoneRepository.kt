package com.spkd.worldclock.domain.repository

import com.spkd.worldclock.data.entity.TimeZone
import kotlinx.coroutines.flow.Flow

interface TimeZoneRepository {
    fun getAllTimeZones(): Flow<List<TimeZone>>
    fun getSelectedTimeZones(): Flow<List<TimeZone>>
    suspend fun getTimeZoneById(uid: String): TimeZone?
    suspend fun insertTimeZone(timeZone: TimeZone)
    suspend fun insertTimeZones(timeZones: List<TimeZone>)
    suspend fun updateTimeZone(timeZone: TimeZone)
    suspend fun deleteTimeZone(timeZone: TimeZone)
    suspend fun deleteAllTimeZones()
    suspend fun updateTimeZoneSelection(uid: String, isSelected: Boolean)
    suspend fun initializeDefaultTimeZones()
}
