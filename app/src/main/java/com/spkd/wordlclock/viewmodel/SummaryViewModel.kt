package com.spkd.wordlclock.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spkd.wordlclock.util.TimeZoneRepositoryInstance
import com.spkd.worldclock.data.entity.TimeZone
import io.reactivex.schedulers.Schedulers

class SummaryViewModel : ViewModel() {


    var selectedTimeZoneList:MutableLiveData<List<TimeZone>> = MutableLiveData()

    fun updateTimeZonesFromDatabase()  {
        val timeZoneRepository = TimeZoneRepositoryInstance.getInstance()
        timeZoneRepository.getAll().observeForever {
            selectedTimeZoneList.postValue(it)
        }
    }

    fun removeTimeZoneFromDatabase(timeZone:String) {
        val timeZoneRepository = TimeZoneRepositoryInstance.getInstance()
        timeZoneRepository.delete(TimeZone(timeZone,timeZone)).subscribeOn(Schedulers.newThread()).subscribe()
    }

}