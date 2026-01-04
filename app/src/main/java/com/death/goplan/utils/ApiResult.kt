package com.death.goplan.utils

sealed interface ApiResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResult<T>

    object Loading : ApiResult<Nothing>
    data class Error<out T: Any>(val error: Throwable) : ApiResult<T>
}