package com.spkd.worldclock.core.usecase

import android.app.Application
import com.spkd.worldclock.core.util.RepositoryInstance
import com.spkd.worldclock.data.repository.ITimeZoneRepository
import javax.inject.Inject


open class BaseUseCase {

    var timeZoneRepository: ITimeZoneRepository = RepositoryInstance.getInstance()

}