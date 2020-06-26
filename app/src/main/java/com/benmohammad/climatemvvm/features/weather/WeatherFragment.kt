package com.benmohammad.climatemvvm.features.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.api.load
import com.benmohammad.climatemvvm.R
import com.benmohammad.climatemvvm.base.*
import com.benmohammad.climatemvvm.custom.aliases.WeatherResult
import com.benmohammad.climatemvvm.custom.errors.ErrorHandler
import com.benmohammad.climatemvvm.custom.views.IndefiniteSnackBar
import com.benmohammad.climatemvvm.extensions.capitalizeWords
import com.benmohammad.climatemvvm.features.home.HomeActivity
import com.benmohammad.climatemvvm.room.models.weather.DbWeather
import com.benmohammad.climatemvvm.utils.Utils
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherFragment: BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val weatherViewModel: WeatherViewModel by viewModels { viewModelFactory }
    private lateinit var cvWeather: MaterialCardView
    private lateinit var pbHome: ContentLoadingProgressBar
    private lateinit var tvCityName: MaterialTextView
    private lateinit var tvWeatherName: MaterialTextView
    private lateinit var tvWeatherInCelsius: MaterialTextView
    private lateinit var weatherIcon: ImageView
    private lateinit var tvWeatherDescription: MaterialTextView
    private lateinit var tvMinTemp: MaterialTextView
    private lateinit var tvMaxTemp: MaterialTextView
    private lateinit var tvSunrise: MaterialTextView
    private lateinit var tvSunset: MaterialTextView
    private lateinit var btnShowForecasts: MaterialButton
    private val observer = Observer<Result<DbWeather>> {handleResponse(it)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return view ?: inflater.inflate(
            R.layout.weather_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        (activity as HomeActivity).setTitle(R.string.weather)
        (activity as HomeActivity).homeComponent?.inject(this)
        weatherViewModel.weatherData.observe(viewLifecycleOwner, observer)
        getWeather()
        super.onActivityCreated(savedInstanceState)

    }

    private fun initViews(view: View) {
        view.apply {
            cvWeather = findViewById(R.id.card_view_weather)
            pbHome = findViewById(R.id.pb_home)
            tvCityName = findViewById(R.id.tv_city_name)
            tvWeatherName = findViewById(R.id.tv_weather_name)
            tvWeatherInCelsius = findViewById(R.id.tv_weather_celsius)
            weatherIcon = findViewById(R.id.weather_icon)
            tvWeatherDescription = findViewById(R.id.tv_weather_description)
            tvMinTemp = findViewById(R.id.tv_min_temp)
            tvMaxTemp = findViewById(R.id.tv_max_temp)
            tvSunrise = findViewById(R.id.tv_sunrise)
            tvSunset = findViewById(R.id.tv_sunset)
            btnShowForecasts = findViewById(R.id.btn_show_forecasts)
        }
        btnShowForecasts.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_forecastsFragment)
        }

    }

    private fun calWeatherApi() {
        IndefiniteSnackBar.hide()
        lifecycleScope.launch {
            weatherViewModel.callWeatherApi()
        }
    }

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
            tvSunrise.text = Utils.getTimeString(weatherData.sys.sunrise)
            tvSunset.text = Utils.getTimeString(weatherData.sys.sunset)
        }

    }}