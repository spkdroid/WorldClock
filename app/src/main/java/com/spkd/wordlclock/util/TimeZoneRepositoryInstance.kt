package com.spkd.wordlclock.util

import com.spkd.worldclock.data.repository.ITimeZoneRepository


object TimeZoneRepositoryInstance {

    private lateinit var instance:ITimeZoneRepository

    fun setInstance(instance:ITimeZoneRepository) {
        TimeZoneRepositoryInstance.instance = instance
    }

    fun getInstance():ITimeZoneRepository {
        return instance
    }
}