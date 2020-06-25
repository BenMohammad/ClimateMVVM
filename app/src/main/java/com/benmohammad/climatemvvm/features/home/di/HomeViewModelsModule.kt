package com.benmohammad.climatemvvm.features.home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benmohammad.climatemvvm.di.ViewModelKey
import com.benmohammad.climatemvvm.di.factories.WeatherViewModelFactory
import com.benmohammad.climatemvvm.features.forecasts.ForecastsViewModel
import com.benmohammad.climatemvvm.features.weather.WeatherViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeViewModelsModule {

    @HomeScope
    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel::class)
    abstract fun bindWeatherViewModel(weatherViewModel: WeatherViewModel): ViewModel


    @HomeScope
    @Binds
    @IntoMap
    @ViewModelKey(ForecastsViewModel::class)
    abstract fun bindForecastsViewModel(forecastsViewModel: ForecastsViewModel): ViewModel

    @HomeScope
    @Binds
    abstract fun bindHomeViewModelFactory(factory: WeatherViewModelFactory): ViewModelProvider.Factory
}