package com.benmohammad.climatemvvm.features.forecasts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benmohammad.climatemvvm.custom.aliases.ForecastResults
import com.benmohammad.climatemvvm.extensions.cancelIfActive
import com.benmohammad.climatemvvm.features.home.di.HomeScope
import com.benmohammad.climatemvvm.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HomeScope
class ForecastsViewModel @Inject constructor(private val forecastsRepository: ForecastsRepository): ViewModel() {

    private val mutableForecastLiveData = MutableLiveData<ForecastResults>()

    private var getForecastsJob: Job? = null

    val forecastLiveData = mutableForecastLiveData

    fun getForecasts() {
        getForecastsJob.cancelIfActive()
        getForecastsJob = viewModelScope.launch {

            forecastsRepository.getForecasts(Utils.LONDON_CITY_ID).collect {

                mutableForecastLiveData.value = it as ForecastResults
            }


        }

}
}