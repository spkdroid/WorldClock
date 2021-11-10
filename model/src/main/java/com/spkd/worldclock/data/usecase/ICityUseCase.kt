package com.spkd.worldclock.data.usecase

import androidx.lifecycle.LiveData
import com.spkd.worldclock.data.entity.TimeZone

interface ICityUseCase {

    fun getAllCity():LiveData<ArrayList<String>>
}