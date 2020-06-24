package com.benmohammad.climatemvvm.room.dao.weather

import androidx.room.*
import com.benmohammad.climatemvvm.room.models.weather.DbWeather
import com.benmohammad.climatemvvm.room.models.weather.Weather
import com.benmohammad.climatemvvm.room.models.weather.WeatherData
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dbWeather: WeatherData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(listWeather: List<Weather>)

    @Delete
    suspend fun delete(dbWeather: WeatherData)

    @Query("DELETE FROM WeatherData")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAllAndInsert(dbWeather: DbWeather) {
        Timber.i("DELETING & INSERTING DATA")
        deleteAll()
        insert(dbWeather.weatherData)
        insertList(dbWeather.list)
    }

    @Transaction
    @Query("SELECT * FROM WeatherData LIMIT 1")
    suspend fun get(): DbWeather?

    @Transaction
    @Query("SELECT * FROM WeatherData LIMIT 1")
    fun getFlow(): Flow<DbWeather?>
}