package com.spkd.wordlclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.spkd.worldclock.core.DaggerAppComponent
import com.spkd.worldclock.core.di.AppModule
import com.spkd.worldclock.core.di.RoomModule

class MainActivity : AppCompatActivity() {

    lateinit var navHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        DaggerAppComponent.builder().appModule(AppModule(application))
            .roomModule(RoomModule(application)).build().inject(this)
    }
}