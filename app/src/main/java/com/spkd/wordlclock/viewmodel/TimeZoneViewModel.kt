package com.spkd.wordlclock.viewmodel

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.spkd.wordlclock.util.TimeZoneRepositoryInstance
import com.spkd.worldclock.core.usecase.CityUseCase
import com.spkd.worldclock.data.entity.TimeZone
import com.spkd.worldclock.data.usecase.ICityUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class TimeZoneViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    fun getCityList() : LiveData<ArrayList<String>> {
        val cityUseCase: ICityUseCase = CityUseCase()
        return cityUseCase.getAllCity()
    }

    fun addTimeZoneToDatabase(text: String) : LiveData<List<TimeZone>> {
         val timeZoneRepository = TimeZoneRepositoryInstance.getInstance()
        timeZoneRepository.insert(TimeZone(text,text)).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe()
        return timeZoneRepository.getAll()
    }


}