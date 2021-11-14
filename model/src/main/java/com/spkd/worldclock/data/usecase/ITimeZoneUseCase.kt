package com.spkd.worldclock.data.usecase

import androidx.lifecycle.LiveData
import com.spkd.worldclock.data.entity.TimeZone

interface ITimeZoneUseCase {

    fun addTimeZone(timeZone:String)

    fun getAllTimeZone():LiveData<List<TimeZone>>

    fun removeTimeZone(timeZone: String)

}