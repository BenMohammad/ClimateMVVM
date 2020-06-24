package com.benmohammad.climatemvvm.room.dao.forecasts

import androidx.room.*
import com.benmohammad.climatemvvm.room.models.forecasts.DbForecast
import com.benmohammad.climatemvvm.room.models.forecasts.Forecast
import com.benmohammad.climatemvvm.room.models.forecasts.ForecastData
import com.benmohammad.climatemvvm.room.models.forecasts.ForecastWeather
import okhttp3.internal.format
import timber.log.Timber

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dbForecast: ForecastData)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(list: Forecast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastList(list: List<Forecast>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherList(list: List<ForecastWeather>)

    @Delete
    suspend fun delete(dbForecast: ForecastData)

    @Query("DELETE FROM ForecastData")
    suspend fun deleteAll()

    @Query("DELETE FROM Forecast")
    suspend fun deleteAllForecasts()

    @Query("DELETE FROM ForecastWeather")
    suspend fun deleteAllForecastWeather()

    @Transaction
    suspend fun deleteAllInsert(dbForecast: DbForecast) {
        Timber.i("DELETING & INSERTING DATA")
        deleteAll()
        deleteAllForecasts()
        deleteAllForecastWeather()
        insert(dbForecast.forecastData)
        dbForecast.list.forEach {
            insertForecast(it.forecast)
            insertWeatherList(it.forecastWeather)
        }
    }

    @Transaction
    @Query("SELECT * FROM ForecastData LIMIT 1")
    suspend fun get(): DbForecast?
}