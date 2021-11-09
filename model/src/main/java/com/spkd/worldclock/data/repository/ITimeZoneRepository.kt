package com.spkd.worldclock.data.repository

import androidx.lifecycle.LiveData
import com.spkd.worldclock.data.entity.TimeZone
import io.reactivex.Completable

interface ITimeZoneRepository {
    fun getAll(): LiveData<List<TimeZone>>

    fun insert(timeZone: TimeZone): Completable

    fun delete(timeZone: TimeZone): Completable

    fun getById(id:Int):LiveData<List<TimeZone>>
}