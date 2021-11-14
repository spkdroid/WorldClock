package com.spkd.worldclock.core.usecase

import com.spkd.worldclock.core.util.RepositoryInstance
import com.spkd.worldclock.data.repository.ITimeZoneRepository


open class BaseUseCase {

    var timeZoneRepository: ITimeZoneRepository = RepositoryInstance.getInstance()

}