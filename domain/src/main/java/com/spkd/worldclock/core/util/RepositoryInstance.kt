package com.spkd.worldclock.core.util

import com.spkd.worldclock.data.repository.ITimeZoneRepository

object RepositoryInstance {

    private lateinit var instance:ITimeZoneRepository

    fun setInstance(instance: ITimeZoneRepository) {
        RepositoryInstance.instance = instance
    }

    fun getInstance():ITimeZoneRepository {
        return instance
    }
}