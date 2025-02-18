package com.example.myapplication.repo

import com.example.myapplication.datastore.GenderDatastore
import com.example.myapplication.entity.Gender
import com.example.myapplication.entity.RequestResult

interface GenderRepo: GenderDatastore {

    fun getAgeList(): List<Int>

    suspend fun sendMessage(
        gender: Gender,
        age: Int,
    ): RequestResult<Boolean>
}
