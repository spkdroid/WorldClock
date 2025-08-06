package com.spkd.worldclock.domain.usecase

import com.spkd.worldclock.domain.repository.TimeZoneRepository
import javax.inject.Inject

class InitializeDefaultTimeZonesUseCase @Inject constructor(
    private val repository: TimeZoneRepository
) {
    suspend operator fun invoke() {
        repository.initializeDefaultTimeZones()
    }
}
