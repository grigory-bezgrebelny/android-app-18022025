package com.example.myapplication.datastore

import com.example.myapplication.entity.Gender
import kotlinx.coroutines.flow.Flow

interface GenderDatastore {
    fun getGenderFlow(): Flow<Gender?>
    suspend fun setGender(value: Gender)
    fun getAgeFlow(): Flow<Int?>
    suspend fun setAge(value: Int)
}
