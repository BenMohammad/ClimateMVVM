package com.benmohammad.climatemvvm.custom.errors

import java.lang.Exception

class NoResponseException(message: String? = ErrorHandler.UNKNOWN_ERROR): Exception(message)

class NoDataException(message: String? = ErrorHandler.NO_SUCH_DATA) : Exception()