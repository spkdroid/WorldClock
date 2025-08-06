package com.spkd.worldclock.data.repository

import com.spkd.worldclock.core.database.TimeZoneDao
import com.spkd.worldclock.data.entity.TimeZone
import com.spkd.worldclock.domain.repository.TimeZoneRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeZoneRepositoryImpl @Inject constructor(
    private val timeZoneDao: TimeZoneDao
) : TimeZoneRepository {

    override fun getAllTimeZones(): Flow<List<TimeZone>> =
        timeZoneDao.getAllTimeZones()

    override fun getSelectedTimeZones(): Flow<List<TimeZone>> =
        timeZoneDao.getSelectedTimeZones()

    override suspend fun getTimeZoneById(uid: String): TimeZone? =
        timeZoneDao.getTimeZoneById(uid)

    override suspend fun insertTimeZone(timeZone: TimeZone) =
        timeZoneDao.insertTimeZone(timeZone)

    override suspend fun insertTimeZones(timeZones: List<TimeZone>) =
        timeZoneDao.insertTimeZones(timeZones)

    override suspend fun updateTimeZone(timeZone: TimeZone) =
        timeZoneDao.updateTimeZone(timeZone)

    override suspend fun deleteTimeZone(timeZone: TimeZone) =
        timeZoneDao.deleteTimeZone(timeZone)

    override suspend fun deleteAllTimeZones() =
        timeZoneDao.deleteAllTimeZones()

    override suspend fun updateTimeZoneSelection(uid: String, isSelected: Boolean) =
        timeZoneDao.updateTimeZoneSelection(uid, isSelected)

    override suspend fun initializeDefaultTimeZones() {
        val defaultTimeZones = listOf(
            TimeZone("america_new_york", "America/New_York", "New York"),
            TimeZone("america_los_angeles", "America/Los_Angeles", "Los Angeles"),
            TimeZone("europe_london", "Europe/London", "London"),
            TimeZone("europe_paris", "Europe/Paris", "Paris"),
            TimeZone("asia_tokyo", "Asia/Tokyo", "Tokyo"),
            TimeZone("asia_shanghai", "Asia/Shanghai", "Shanghai"),
            TimeZone("asia_kolkata", "Asia/Kolkata", "Mumbai"),
            TimeZone("australia_sydney", "Australia/Sydney", "Sydney"),
            TimeZone("america_sao_paulo", "America/Sao_Paulo", "São Paulo"),
            TimeZone("africa_cairo", "Africa/Cairo", "Cairo"),
            TimeZone("europe_moscow", "Europe/Moscow", "Moscow"),
            TimeZone("asia_dubai", "Asia/Dubai", "Dubai")
        )
        insertTimeZones(defaultTimeZones)
    }
}
