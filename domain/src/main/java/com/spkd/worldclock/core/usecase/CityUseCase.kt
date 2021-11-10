package com.spkd.worldclock.core.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spkd.worldclock.data.usecase.ICityUseCase
import java.util.*
import kotlin.collections.ArrayList

class CityUseCase : ICityUseCase {
    override fun getAllCity(): LiveData<ArrayList<String>> {
        val timeZoneLiveData = MutableLiveData<ArrayList<String>>()
        val result: ArrayList<String> = ArrayList()
        val timeZoneId = TimeZone.getAvailableIDs() as Array<String>

        timeZoneId.forEach {
            result.add(it)
        }

        timeZoneLiveData.postValue(result)

        return timeZoneLiveData
    }
}