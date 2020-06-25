package com.benmohammad.climatemvvm.custom.aliases

import com.benmohammad.climatemvvm.room.models.forecasts.Forecast
import com.benmohammad.climatemvvm.base.Result
import com.benmohammad.climatemvvm.room.models.weather.DbWeather

typealias WeatherResult = Result<DbWeather>

typealias ListOfForecasts = List<Forecast>

typealias ForecastResults = Result<ListOfForecasts>