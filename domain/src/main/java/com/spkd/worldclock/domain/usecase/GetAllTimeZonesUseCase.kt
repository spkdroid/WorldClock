package com.spkd.worldclock.domain.usecase

import com.spkd.worldclock.data.entity.TimeZone
import com.spkd.worldclock.domain.repository.TimeZoneRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTimeZonesUseCase @Inject constructor(
    private val repository: TimeZoneRepository
) {
    operator fun invoke(): Flow<List<TimeZone>> = repository.getAllTimeZones()
}
