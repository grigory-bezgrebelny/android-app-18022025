package com.example.myapplication.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.myapplication.entity.Gender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DefaultGenderDatastore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : GenderDatastore {

    private val genderKey = stringPreferencesKey("gender")
    private val age = intPreferencesKey("age")

    override fun getGenderFlow(): Flow<Gender?> {
        return dataStore.data.map { pref ->
            pref[genderKey]?.let(Json::decodeFromString)
        }
    }

    override suspend fun setGender(value: Gender) {
        dataStore.edit { pref ->
            pref[genderKey] = Json.encodeToString(value)
        }
    }

    override fun getAgeFlow(): Flow<Int?> {
        return dataStore.data.map { pref ->
            pref[age]
        }
    }

    override suspend fun setAge(value: Int) {
        dataStore.edit { pref ->
            pref[age] = value
        }
    }
}
