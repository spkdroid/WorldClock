package com.spkd.worldclock.core.usecase

import androidx.lifecycle.LiveData
import com.spkd.worldclock.data.entity.TimeZone
import com.spkd.worldclock.data.usecase.ITimeZoneUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TimeZoneUseCase : BaseUseCase(), ITimeZoneUseCase {

    override fun addTimeZone(timeZone: String) {
        timeZoneRepository.insert(TimeZone(timeZone, timeZone)).subscribeOn(Schedulers.io())
            .observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe()
    }

    override fun getAllTimeZone(): LiveData<List<TimeZone>> {
        return timeZoneRepository.getAll()
    }

    override fun removeTimeZone(timeZone: String) {
        timeZoneRepository.delete(TimeZone(timeZone, timeZone)).subscribeOn(Schedulers.newThread())
            .subscribe()
    }
}