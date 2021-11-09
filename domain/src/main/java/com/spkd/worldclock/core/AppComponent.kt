package com.spkd.worldclock.core

import android.app.Activity
import android.app.Application
import com.spkd.worldclock.core.database.TimeZoneDao
import com.spkd.worldclock.core.database.TimeZoneDatabase
import com.spkd.worldclock.core.di.AppModule
import com.spkd.worldclock.core.di.RoomModule
import com.spkd.worldclock.data.repository.ITimeZoneRepository
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(dependencies = [], modules = [AppModule::class, RoomModule::class])
public interface AppComponent {

    fun inject(app: Activity)

    fun timeZoneDao(): TimeZoneDao

    fun timeZoneDatabase(): TimeZoneDatabase

    fun timeZoneRepository(): ITimeZoneRepository

    fun application(): Application
}