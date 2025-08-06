package com.spkd.worldclock.di

import android.content.Context
import androidx.room.Room
import com.spkd.worldclock.core.database.TimeZoneDao
import com.spkd.worldclock.core.database.TimeZoneDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTimeZoneDatabase(
        @ApplicationContext context: Context
    ): TimeZoneDatabase {
        return Room.databaseBuilder(
            context,
            TimeZoneDatabase::class.java,
            TimeZoneDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideTimeZoneDao(database: TimeZoneDatabase): TimeZoneDao {
        return database.timeZoneDao()
    }
}
