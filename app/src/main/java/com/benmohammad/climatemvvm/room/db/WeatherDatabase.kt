package com.benmohammad.climatemvvm.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.benmohammad.climatemvvm.room.dao.forecasts.ForecastDao
import com.benmohammad.climatemvvm.room.dao.utils.StringKeyValueDao
import com.benmohammad.climatemvvm.room.dao.weather.WeatherDao
import com.benmohammad.climatemvvm.room.models.forecasts.Forecast
import com.benmohammad.climatemvvm.room.models.forecasts.ForecastData
import com.benmohammad.climatemvvm.room.models.forecasts.ForecastWeather
import com.benmohammad.climatemvvm.room.models.utils.StringKeyValuePair
import com.benmohammad.climatemvvm.room.models.weather.Weather
import com.benmohammad.climatemvvm.room.models.weather.WeatherData

@Database(
    entities = [WeatherData::class, Weather::class, ForecastData::class, Forecast::class,
                ForecastWeather::class, StringKeyValuePair::class], version = 1
)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun keyValueDao(): StringKeyValueDao
    abstract fun forecastDao(): ForecastDao
}