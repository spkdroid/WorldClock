package com.spkd.worldclock.core.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(app: Application) {

    var application:Application = app

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }
}