package com.benmohammad.climatemvvm.features.weather

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benmohammad.climatemvvm.custom.aliases.WeatherResult
import com.benmohammad.climatemvvm.extensions.cancelIfActive
import com.benmohammad.climatemvvm.features.home.di.HomeScope
import com.benmohammad.climatemvvm.room.models.weather.DbWeather
import com.benmohammad.climatemvvm.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HomeScope
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel() {

    private val mutableWeatherLiveData = MutableLiveData<WeatherResult>()
    private var getWeatherJob: Job? = null

    val weatherData = mutableWeatherLiveData

//    private val mutableWeatherData = MutableLiveData<DbWeather>()
//
//    val weatherData = mutableWeatherData
//
//    init{
//        viewModelScope.launch {
//            WeatherRepository.getWeather(Utils.LONDON_CITY).collect {
//                mutableWeatherLiveData.value = it
//            }
//        }
//    }

    fun getWeather() {
        getWeatherJob.cancelIfActive()
        getWeatherJob = viewModelScope.launch {
            weatherRepository.getWeather(Utils.LONDON_CITY).collect {
                mutableWeatherLiveData.value = it
            }
        }
    }

    @MainThread
    suspend fun callWeatherApi() {
        weatherRepository.callWeatherApi(Utils.LONDON_CITY).collect {
            mutableWeatherLiveData.value = it

        }    }
}