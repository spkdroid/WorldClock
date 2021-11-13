package com.spkd.wordlclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.spkd.wordlclock.util.TimeZoneRepositoryInstance
import com.spkd.worldclock.core.di.AppModule
import com.spkd.worldclock.core.di.RoomModule
import com.spkd.worldclock.data.repository.ITimeZoneRepository
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var navHost: NavHostFragment

    @Inject
    lateinit var timeZoneRepository: ITimeZoneRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        DaggerAppComponent.builder().appModule(AppModule(application))
            .roomModule(RoomModule(application)).build().inject(this)

        TimeZoneRepositoryInstance.setInstance(timeZoneRepository)
    }
}