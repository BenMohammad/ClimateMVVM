package com.benmohammad.climatemvvm.features.home.di

import com.benmohammad.climatemvvm.features.forecasts.ForecastsFragment
import com.benmohammad.climatemvvm.features.home.HomeActivity
import com.benmohammad.climatemvvm.features.weather.WeatherFragment
import dagger.Subcomponent

@HomeScope
@Subcomponent(modules = [HomeViewModelsModule::class])
interface HomeComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }

    fun inject(homeActivity: HomeActivity)
    fun inject(weatherFragment: WeatherFragment)
    fun inject(forecastsFragment: ForecastsFragment)
}