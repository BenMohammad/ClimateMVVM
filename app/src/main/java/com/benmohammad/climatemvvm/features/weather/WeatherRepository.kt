package com.benmohammad.climatemvvm.features.weather

import com.benmohammad.climatemvvm.base.*
import com.benmohammad.climatemvvm.custom.aliases.WeatherResult
import com.benmohammad.climatemvvm.custom.errors.ErrorHandler
import com.benmohammad.climatemvvm.custom.errors.NoDataException
import com.benmohammad.climatemvvm.custom.errors.NoResponseException
import com.benmohammad.climatemvvm.entitymappers.weather.WeatherMapper
import com.benmohammad.climatemvvm.features.home.di.HomeScope
import com.benmohammad.climatemvvm.network.api.OpenWeatherApi
import com.benmohammad.climatemvvm.network.response.ErrorResponse
import com.benmohammad.climatemvvm.room.dao.utils.StringKeyValueDao
import com.benmohammad.climatemvvm.room.dao.weather.WeatherDao
import com.benmohammad.climatemvvm.utils.Utils
import com.benmohammad.climatemvvm.extensions.applyCommonSideEffects
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HomeScope
class WeatherRepository @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val weatherDao: WeatherDao,
    private val stringKeyValueDao: StringKeyValueDao
) {

    private val weatherCacheThresholdMillis = 3600000L

    fun getWeather(cityName: String) = flow {
        stringKeyValueDao.get(Utils.LAST_WEATHER_API_CALL_TIMESTAMP)
            ?.takeIf { !Utils.shouldCallApi(it.value, weatherCacheThresholdMillis) }
            ?.let { emit(getDataOrError(NoDataException())) }
            ?: emit(getWeatherFromAPI(cityName))
    }
        .applyCommonSideEffects()
        .catch {
            emit(getDataOrError(it))
        }



    fun callWeatherApi(cityName: String) = flow<WeatherResult> {
        val lastTimestamp = stringKeyValueDao.get(Utils.LAST_WEATHER_API_CALL_TIMESTAMP)
        if (lastTimestamp == null || Utils.shouldCallApi(
                lastTimestamp.value,
                weatherCacheThresholdMillis
            )
        ) {
            openWeatherApi.getWeatherFromCityName(cityName)
                .run {
                    if (isSuccessful && body() != null) {
                        stringKeyValueDao.insert(
                            Utils.getCurrentTimeKeyValuePair(Utils.LAST_WEATHER_API_CALL_TIMESTAMP)
                        )
                        weatherDao.deleteAllAndInsert(WeatherMapper(body()!!).map())
                    }
                }
        }
    }.applyCommonSideEffects().catch { emit(Error(it)) }


    private suspend fun getWeatherFromAPI(cityName: String) =
        openWeatherApi.getWeatherFromCityName(cityName)
            .run {
                if(isSuccessful && body() != null) {
                    stringKeyValueDao.insert(
                        Utils.getCurrentTimeKeyValuePair(Utils.LAST_WEATHER_API_CALL_TIMESTAMP)
                    )
                    weatherDao.deleteAllAndInsert(WeatherMapper(body()!!).map())
                    getDataOrError(NoDataException())
                } else {
                    Error(
                        NoResponseException(
                            ErrorHandler.parseError<ErrorResponse>(errorBody())?.message)
                        )
                }
            }

                private suspend fun getDataOrError(throwable: Throwable) =
                    weatherDao.get()
                        ?.let { dbValue -> Success(dbValue) }
                        ?: Error(throwable)


    fun getWeatherDBFlow() = weatherDao.getFlow()
}