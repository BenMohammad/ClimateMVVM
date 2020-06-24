package com.benmohammad.climatemvvm.base

import com.benmohammad.climatemvvm.custom.errors.ErrorHandler

sealed class Result<out T : Any>

class Success<out T : Any> : Result<T>()

class Error(
    val exception: Throwable,
    val message: String = exception.message ?: ErrorHandler.UNKNOWN_ERROR
): Result<Nothing>()

class Progress(val isLoading: Boolean): Result<Nothing>()