package com.spkd.worldclock.di

import com.spkd.worldclock.data.repository.TimeZoneRepositoryImpl
import com.spkd.worldclock.domain.repository.TimeZoneRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTimeZoneRepository(
        timeZoneRepositoryImpl: TimeZoneRepositoryImpl
    ): TimeZoneRepository
}
