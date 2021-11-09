package com.spkd.worldclock.core.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.spkd.worldclock.data.entity.TimeZone
import io.reactivex.Completable


@Dao
interface TimeZoneDao {
    @Query("SELECT * FROM TimeZone")
    fun getAll(): LiveData<List<TimeZone>>

    @Query("SELECT * FROM TimeZone WHERE uid IN (:userIds)")
    fun findById(userIds: Int): LiveData<List<TimeZone>>

    @Insert
    fun insert(timeZone: TimeZone) : Completable

    @Delete
    fun delete(timeZone: TimeZone): Completable
}