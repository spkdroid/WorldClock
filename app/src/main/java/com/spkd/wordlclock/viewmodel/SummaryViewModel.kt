package com.spkd.wordlclock.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spkd.wordlclock.util.TimeZoneRepositoryInstance
import com.spkd.worldclock.core.usecase.CityUseCase
import com.spkd.worldclock.data.entity.TimeZone
import com.spkd.worldclock.data.usecase.ICityUseCase

class SummaryViewModel : ViewModel() {


    var selectedTimeZoneList:MutableLiveData<List<TimeZone>> = MutableLiveData()

    fun updateTimeZonesFromDatabase()  {
        val timeZoneRepository = TimeZoneRepositoryInstance.getInstance()
        timeZoneRepository.getAll().observeForever {
            selectedTimeZoneList.postValue(it)
        }
    }
}