package com.example.myapplication.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestRequest(
    @SerialName("gender")
    val gender: Gender,
    @SerialName("age")
    val age: Int
)
