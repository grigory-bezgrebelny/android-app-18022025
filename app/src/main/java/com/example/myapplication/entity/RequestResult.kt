package com.example.myapplication.entity

import com.example.myapplication.core.UiText

sealed interface RequestResult<out T> {
    class Success<T>(val value: T) : RequestResult<T>
    class Error(val value: UiText) : RequestResult<Nothing>
}

fun <T> T.asSuccess(): RequestResult.Success<T> = RequestResult.Success(this)

fun UiText.asError(): RequestResult.Error = RequestResult.Error(this)
