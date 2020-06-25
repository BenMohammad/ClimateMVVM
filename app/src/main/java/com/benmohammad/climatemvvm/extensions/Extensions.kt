package com.benmohammad.climatemvvm.extensions

import com.benmohammad.climatemvvm.base.Progress
import com.benmohammad.climatemvvm.base.Result
import com.benmohammad.climatemvvm.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import java.io.IOException




fun String.capitalizeWords(): String = this.split(' ').joinToString(" "){it.capitalize()}

@PublishedApi
internal inline fun Retrofit.Builder.callFactory(crossinline body: (Request) -> Call) =
    callFactory(object: Call.Factory {
        override fun newCall(request: Request): Call = body(request)
    })

@Suppress("NOTHING_TO_INLINE")
inline fun Retrofit.Builder.delegatingCallFactory(delegate: dagger.Lazy<OkHttpClient>): Retrofit.Builder =
    callFactory {
        delegate.get().newCall(it) }

fun <T: Any> Flow<Result<T>>.applyCommonSideEffects() =
    retryWhen {  cause, attempt ->
        when {
            (cause is IOException && attempt < Utils.MAX_RETRIES) -> {
                delay(Utils.getBackOffDelay(attempt))
                true
            }
            else -> {
                false
            }
        }
    }
        .onStart { emit(Progress(isLoading = true)) }
        .onCompletion { emit(Progress(isLoading = false)) }




    fun Job?.cancelIfActive() {
        if(this?.isActive == true) {
        cancel()
    }
}



