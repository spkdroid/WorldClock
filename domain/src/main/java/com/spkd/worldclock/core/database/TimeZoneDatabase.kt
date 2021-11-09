package com.spkd.worldclock.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spkd.worldclock.data.entity.TimeZone


@Database(entities = [TimeZone::class], version = 1)
abstract class TimeZoneDatabase : RoomDatabase() {
    abstract fun timeZoneDao(): TimeZoneDao
}