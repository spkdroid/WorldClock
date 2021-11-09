package com.spkd.worldclock.core.di

import android.app.Application
import androidx.room.Room
import com.spkd.worldclock.core.database.TimeZoneDao
import com.spkd.worldclock.core.database.TimeZoneDatabase
import com.spkd.worldclock.core.repository.TimeZoneDataSource
import com.spkd.worldclock.data.repository.ITimeZoneRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
public class RoomModule(application: Application) {

    var timeZoneDatabase =
        Room.databaseBuilder(application, TimeZoneDatabase::class.java, "timezone-db").build()

    @Singleton
    @Provides
    fun provideDatabase(): TimeZoneDatabase {
        return timeZoneDatabase
    }

    @Singleton
    @Provides
    fun providesDatabaseDao(timeZoneDatabase: TimeZoneDatabase): TimeZoneDao {
        return timeZoneDatabase.timeZoneDao()
    }

    @Singleton
    @Provides
    fun radioRepository(timeZoneDao: TimeZoneDao): ITimeZoneRepository {
        return TimeZoneDataSource(timeZoneDao)
    }
}