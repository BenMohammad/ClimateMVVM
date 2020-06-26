package com.benmohammad.climatemvvm.features.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.benmohammad.climatemvvm.R
import com.benmohammad.climatemvvm.WeatherApplication
import com.benmohammad.climatemvvm.features.home.di.HomeComponent
import com.benmohammad.climatemvvm.room.models.weather.Weather

class HomeActivity: AppCompatActivity() {


    var homeComponent: HomeComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeComponent = (applicationContext as WeatherApplication).appComponent.homeComponent().create()
        homeComponent?.inject(this)
    }
}