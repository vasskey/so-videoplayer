package com.silverorange.videoplayer

sealed class DataState<T> {
    class Loading<T> : DataState<T>()

    class Error<T>(val exception: Exception? = null) : DataState<T>()

    data class Ready<T>(val data: T) : DataState<T>()
}
