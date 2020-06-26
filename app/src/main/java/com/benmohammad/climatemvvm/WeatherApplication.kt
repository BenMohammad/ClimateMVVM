package com.benmohammad.climatemvvm

import android.app.AppComponentFactory
import android.app.Application
import com.benmohammad.climatemvvm.di.component.ApplicationComponent
import com.benmohammad.climatemvvm.di.component.DaggerApplicationComponent
import com.benmohammad.climatemvvm.di.modules.AppModule
import com.benmohammad.climatemvvm.di.modules.DbModule
import com.benmohammad.climatemvvm.di.modules.OpenWeatherApiModule
import com.squareup.moshi.Moshi
import timber.log.Timber

class WeatherApplication: Application() {

    lateinit var appComponent: ApplicationComponent

    companion object{
        lateinit var moshi: Moshi
    }

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
        initDaggerComponent()
        moshi = appComponent.getMoshi()
    }

    private fun initDaggerComponent() {
        appComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(applicationContext))
            .dbModule(DbModule())
            .openWeatherApiModule(OpenWeatherApiModule())
            .build()
    }
}