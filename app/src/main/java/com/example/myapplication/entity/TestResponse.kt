package com.example.myapplication.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestResponse(
    @SerialName("allowed")
    val allowed: Boolean
)
