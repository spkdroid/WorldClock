package com.spkd.wordlclock.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.spkd.worldclock.core.usecase.CityUseCase
import com.spkd.worldclock.core.usecase.TimeZoneUseCase
import com.spkd.worldclock.data.entity.TimeZone
import com.spkd.worldclock.data.usecase.ICityUseCase
import com.spkd.worldclock.data.usecase.ITimeZoneUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class TimeZoneViewModel : ViewModel() {

    fun getCityList() : LiveData<ArrayList<String>> {
        val cityUseCase: ICityUseCase = CityUseCase()
        return cityUseCase.getAllCity()
    }

    fun addTimeZoneToDatabase(text: String) : LiveData<List<TimeZone>> {
        val timeZoneUseCase: ITimeZoneUseCase = TimeZoneUseCase()
        timeZoneUseCase.addTimeZone(text)
        return timeZoneUseCase.getAllTimeZone()
    }


}