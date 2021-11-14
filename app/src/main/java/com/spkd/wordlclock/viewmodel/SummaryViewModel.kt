package com.spkd.wordlclock.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spkd.worldclock.core.usecase.TimeZoneUseCase
import com.spkd.worldclock.data.entity.TimeZone
import com.spkd.worldclock.data.usecase.ITimeZoneUseCase
import io.reactivex.schedulers.Schedulers

class SummaryViewModel : ViewModel() {

    var selectedTimeZoneList:MutableLiveData<List<TimeZone>> = MutableLiveData()

    fun updateTimeZonesFromDatabase()  {
        val timeZoneUseCase: ITimeZoneUseCase = TimeZoneUseCase()

        timeZoneUseCase.getAllTimeZone().observeForever {
            selectedTimeZoneList.postValue(it)
        }
    }

    fun removeTimeZoneFromDatabase(timeZone:String) {
        val timeZoneUseCase: ITimeZoneUseCase = TimeZoneUseCase()
        timeZoneUseCase.removeTimeZone(timeZone)
    }

}