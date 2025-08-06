package com.spkd.worldclock.domain.usecase

import com.spkd.worldclock.domain.repository.TimeZoneRepository
import javax.inject.Inject

class UpdateTimeZoneSelectionUseCase @Inject constructor(
    private val repository: TimeZoneRepository
) {
    suspend operator fun invoke(uid: String, isSelected: Boolean) {
        repository.updateTimeZoneSelection(uid, isSelected)
    }
}
