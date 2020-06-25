package com.benmohammad.climatemvvm.features.forecasts

import com.benmohammad.climatemvvm.base.Success
import com.benmohammad.climatemvvm.custom.errors.ErrorHandler
import com.benmohammad.climatemvvm.custom.errors.NoDataException
import com.benmohammad.climatemvvm.custom.errors.NoResponseException
import com.benmohammad.climatemvvm.entitymappers.forecasts.ForecastMapper
import com.benmohammad.climatemvvm.features.home.di.HomeScope
import com.benmohammad.climatemvvm.network.api.OpenWeatherApi
import com.benmohammad.climatemvvm.network.response.ErrorResponse
import com.benmohammad.climatemvvm.room.dao.forecasts.ForecastDao
import com.benmohammad.climatemvvm.room.dao.utils.StringKeyValueDao
import com.benmohammad.climatemvvm.room.models.forecasts.DbForecast
import com.benmohammad.climatemvvm.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HomeScope
class ForecastsRepository @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val forecastDao: ForecastDao,
    private val stringKeyValueDao: StringKeyValueDao
) {

    private  val forecastCacheThresholdMillis = 3 * 300000L


    fun getForecasts(cityId: Int) = flow {
        stringKeyValueDao.get(Utils.LAST_FORECASTS_API_CALL_TIMESTAMP)
            ?.takeIf { !Utils.shouldCallApi(it.value, forecastCacheThresholdMillis) }
            ?.let { emit(getDataOrError(NoDataException())) }
            ?: emit((getForecastFromApi(cityId)))

            }



                  .catch{ emit(getDataOrError(it))

                  }





    private suspend fun  getForecastFromApi(cityId: Int) = openWeatherApi
        .getWeatherForecast(cityId)
        .run {
            if(isSuccessful && body() != null) {
                stringKeyValueDao.insert(
                    Utils.getCurrentTimeKeyValuePair(Utils.LAST_FORECASTS_API_CALL_TIMESTAMP)
                )
                forecastDao.deleteAllAndInsert(ForecastMapper(body()!!).map())
                getDataOrError(NoDataException())
            } else {
                Error(
                    NoResponseException(
                        ErrorHandler.parseError<ErrorResponse>(errorBody())?.message))
            }
        }

    private suspend fun getDataOrError(throwable: Throwable) =
        forecastDao.get()
        ?.let { dbValue -> Success(getForecastList(dbValue)) }
        ?: Error(throwable)


    private suspend fun getForecastList(dbForecast: DbForecast) = withContext(Dispatchers.Default) {
        dbForecast.list.map { it.forecast }

    }
}















