package com.spkd.worldclock.core.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.spkd.worldclock.data.entity.TimeZone

@Database(
    entities = [TimeZone::class],
    version = 1,
    exportSchema = false
)
abstract class TimeZoneDatabase : RoomDatabase() {
    abstract fun timeZoneDao(): TimeZoneDao

    companion object {
        const val DATABASE_NAME = "timezone_database"
    }
}