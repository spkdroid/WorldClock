package com.spkd.worldclock.core.util

import com.spkd.worldclock.data.repository.ITimeZoneRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryInstanceTest {

    var timeZoneRepository: ITimeZoneRepository = mock(ITimeZoneRepository::class.java)
    var modifiedTimeZoneRepository: ITimeZoneRepository = mock(ITimeZoneRepository::class.java)

    var repositoryInstance = RepositoryInstance

    @Before
    fun setUp() {
        repositoryInstance.setInstance(timeZoneRepository)
    }

    @Test
    fun getInstanceTest() {
        Assert.assertTrue(repositoryInstance.getInstance() == timeZoneRepository)
    }


    @Test
    fun setInstanceTest() {
        repositoryInstance.setInstance(modifiedTimeZoneRepository)
        Assert.assertTrue(repositoryInstance.getInstance() == modifiedTimeZoneRepository)
    }

}