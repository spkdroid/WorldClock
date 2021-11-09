package com.spkd.worldclock.core.repository

import androidx.lifecycle.LiveData
import com.spkd.worldclock.core.database.TimeZoneDao
import com.spkd.worldclock.data.entity.TimeZone
import com.spkd.worldclock.data.repository.ITimeZoneRepository
import io.reactivex.Completable
import javax.inject.Inject

class TimeZoneDataSource @Inject constructor(var timeZoneDao: TimeZoneDao) : ITimeZoneRepository {

    override fun getAll(): LiveData<List<TimeZone>> {
        return timeZoneDao.getAll()
    }

    override fun insert(timeZone: TimeZone): Completable {
        return timeZoneDao.insert(timeZone)
    }

    override fun delete(timeZone: TimeZone): Completable {
        return timeZoneDao.delete(timeZone)
    }

    override fun getById(id: Int): LiveData<List<TimeZone>> {
        return timeZoneDao.findById(id)
    }


}