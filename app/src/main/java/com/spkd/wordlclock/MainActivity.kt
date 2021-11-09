package com.spkd.wordlclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spkd.worldclock.core.DaggerAppComponent
import com.spkd.worldclock.core.di.AppModule
import com.spkd.worldclock.core.di.RoomModule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerAppComponent.builder().appModule(AppModule(application))
            .roomModule(RoomModule(application)).build().inject(this)
    }
}