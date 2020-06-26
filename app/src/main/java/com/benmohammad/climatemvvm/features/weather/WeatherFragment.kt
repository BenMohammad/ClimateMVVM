package com.benmohammad.climatemvvm.features.weather

import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.benmohammad.climatemvvm.base.*
import com.benmohammad.climatemvvm.custom.aliases.WeatherResult
import com.benmohammad.climatemvvm.custom.errors.ErrorHandler
import com.benmohammad.climatemvvm.custom.views.IndefiniteSnackBar
import com.benmohammad.climatemvvm.extensions.capitalizeWords
import com.benmohammad.climatemvvm.room.models.weather.DbWeather
import com.benmohammad.climatemvvm.utils.Utils
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import javax.inject.Inject

class WeatherFragment: BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val weatherViewModel: WeatherViewModel by viewModels { viewModelFactory }
    private lateinit var cvWeather: MaterialCheckBox
    private lateinit var pbHome: ContentLoadingProgressBar
    private lateinit var tvCityName: MaterialTextView
    private lateinit var tvWeatherName: MaterialTextView
    private lateinit var tvWeatherInCelsius: MaterialTextView
    private lateinit var weatherIcon: ImageView
    private lateinit var tvWeatherDescription: MaterialTextView
    private lateinit var tvMinTemp: MaterialTextView
    private lateinit var tvMaxTemp: MaterialTextView
    private lateinit var tvMSunrise: MaterialTextView
    private lateinit var tvMSunset: MaterialTextView
    private lateinit var btnShowForecasts: MaterialTextView
    private val observer = Observer<Result<DbWeather>> {handleResponse(it)}



    private fun handleResponse(result: WeatherResult) {
        when(result) {
            is Success<DbWeather> -> bindData(result.data)
            is Error -> {
                view?.let {
                    view -> ErrorHandler.handleError(
                    view,
                    result,
                    shouldShowSnackBar = true,
                    refreshAction = {getWeather()}
                )
                }
            }
            is Progress -> {
                pbHome.visibility = toggleVisibility(result)
            }
        }

    }

    private fun getWeather() {
        IndefiniteSnackBar.hide()
        weatherViewModel.getWeather()

    }
    private fun bindData(response: DbWeather) {
        with(response) {
            tvCityName.text = weatherData.name
            tvWeatherInCelsius.text = Utils.getTemperature(weatherData.main.temp)
            list.takeIf { it.isNotEmpty() }
                ?.get(0)
                ?.let {
                    tvWeatherName.text = it.main
                    weatherIcon.load("http://openweathermap.org/img/w/${it.icon}.png")
                    tvWeatherDescription.text = it.description.capitalizeWords()
                }
            tvMinTemp.text = Utils.getTemperature(weatherData.main.tempMin)
            tvMaxTemp.text = Utils.getTemperature(weatherData.main.tempMax)
            tvMSunrise.text = Utils.getTimeString(weatherData.sys.sunrise)
            tvMSunset.text = Utils.getTimeString(weatherData.sys.sunset)
        }

    }}