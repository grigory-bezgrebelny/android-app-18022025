package com.example.myapplication.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Gender {
    @SerialName("m")
    MALE,
    @SerialName("f")
    FEMALE;
}
