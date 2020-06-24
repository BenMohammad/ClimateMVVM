package com.benmohammad.climatemvvm

import android.app.AppComponentFactory
import android.app.Application
import com.squareup.moshi.Moshi
import timber.log.Timber

class WeatherApplication: Application() {

    //lateinit var appComponent = ApplicationComponenet()

    companion object{
        lateinit var moshi: Moshi
    }

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

    }
}