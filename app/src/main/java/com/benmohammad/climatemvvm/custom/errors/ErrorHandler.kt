package com.benmohammad.climatemvvm.custom.errors

import android.content.Context
import android.view.View
import android.widget.Toast
import com.benmohammad.climatemvvm.WeatherApplication
import com.benmohammad.climatemvvm.base.Error
import com.benmohammad.climatemvvm.custom.views.IndefiniteSnackBar
import okhttp3.internal.http.RealResponseBody
import org.json.JSONException
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

object ErrorHandler {

    private const val NETWORK_ERROR_MESSAGE = "Please check your internet connection"

    private const val EMPTY_RESPONSE = "Server returned empty response"
    const val NO_SUCH_DATA = "Data not found in the database"
    const val UNKNOWN_ERROR = "An error occurred"

    fun handleError(
        view: View,
        throwable: Error,
        shouldToast: Boolean = false,
        shouldShowSnackBar: Boolean,
        refreshAction: () -> Unit = {}
        ) {

        if(shouldShowSnackBar) {
            showSnackBar(view, message = throwable.message, refresh = refreshAction)
        } else {
            if(shouldToast) {
                showLongToast(view.context, throwable.message)
            }
        }
        when(throwable.exception) {
            is IOException -> Timber.e(NETWORK_ERROR_MESSAGE)
            is HttpException -> Timber.e(
                "HTTP Exception ${throwable.exception.code()}"
            )
            is NoResponseException -> Timber.e(NO_SUCH_DATA)
            is NoDataException -> Timber.e(throwable.message)
            else -> Timber.e(throwable.message)
        }
    }

    private fun showSnackBar(view: View, message: String, refresh: () -> Unit = {}) {
        IndefiniteSnackBar.show(view, message, refresh)
    }

    private fun showLongToast(context: Context, message: String) = Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()

    inline fun<reified T> parseError(responseBody: RealResponseBody?): T? {
        val parser = WeatherApplication.moshi.adapter(T::class.java)
        val response = responseBody?.string()
        if(response != null)
            try {
                return parser.fromJson(response)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return null
        }

    }