package com.benmohammad.climatemvvm.di.modules

import android.content.Context
import androidx.room.Room
import com.benmohammad.climatemvvm.room.dao.forecasts.ForecastDao
import com.benmohammad.climatemvvm.room.dao.weather.WeatherDao
import com.benmohammad.climatemvvm.room.db.WeatherDatabase
import com.benmohammad.climatemvvm.utils.Utils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideWeatherDB(context: Context): WeatherDatabase {
        return Room.databaseBuilder(context, WeatherDatabase::class.java, Utils.DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(weatherDatabase: WeatherDatabase) =
        weatherDatabase.weatherDao()

    @Provides
    @Singleton
    fun provideForecastDao(weatherDatabase: WeatherDatabase) =
        weatherDatabase.forecastDao()

    @Provides
    @Singleton
    fun provideStringKeyValueDao(weatherDatabase: WeatherDatabase) =
        weatherDatabase.keyValueDao()
}