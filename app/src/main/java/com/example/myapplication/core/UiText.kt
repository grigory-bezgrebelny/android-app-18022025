package com.example.myapplication.core

import android.content.Context

sealed interface UiText {

    fun asString(context: Context): String

    data class Text(val value: String) : UiText {
        override fun asString(context: Context): String = value
    }

    data class Res(val value: Int) : UiText {
        override fun asString(context: Context): String = context.getString(value)
    }
}
