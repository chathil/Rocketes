package com.astro.rocketapp.data.vo

sealed class Resource<T> {
    class Loading<T> : Resource<T>()

    data class Success<T>(val data: T) : Resource<T>()

    data class Error<T>(val error: Throwable) : Resource<T>()
}
