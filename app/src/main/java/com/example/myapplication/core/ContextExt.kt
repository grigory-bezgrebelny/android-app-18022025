package com.example.myapplication.core

import android.content.Context
import android.util.TypedValue

fun Context.toDp(px: Int): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    px.toFloat(),
    resources.displayMetrics
).toInt()
