package com.spkd.wordlclock.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spkd.worldclock.core.usecase.CityUseCase
import com.spkd.worldclock.data.usecase.ICityUseCase

class TimeZoneViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    fun getCityList() : LiveData<ArrayList<String>> {
        val cityUseCase: ICityUseCase = CityUseCase()
        return cityUseCase.getAllCity()
    }

}