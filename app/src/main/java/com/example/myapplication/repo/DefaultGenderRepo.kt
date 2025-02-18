package com.example.myapplication.repo

import com.example.myapplication.R
import com.example.myapplication.core.UiText
import com.example.myapplication.datastore.GenderDatastore
import com.example.myapplication.entity.Gender
import com.example.myapplication.entity.RequestResult
import com.example.myapplication.network.SocketManager
import com.example.myapplication.entity.TestRequest
import com.example.myapplication.entity.asError
import com.example.myapplication.entity.asSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultGenderRepo @Inject constructor(
    private val socketManager: SocketManager,
    private val genderDatastore: GenderDatastore,
): GenderRepo, GenderDatastore by genderDatastore {

    companion object {
        private const val AGE_FROM = 16
        private const val AGE_TO = 30
    }

    override fun getAgeList(): List<Int> {
        return (AGE_FROM..AGE_TO).toList()
    }

    override suspend fun sendMessage(
        gender: Gender,
        age: Int,
    ): RequestResult<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val connected = socketManager.connect()
                if (connected) {
                    val request = TestRequest(gender, age)
                    socketManager.send(request)
                    socketManager.receive().allowed.asSuccess()
                } else {
                    UiText.Res(R.string.no_connection).asError()
                }
            } catch (e: Exception) {
                UiText.Text(e.message.toString()).asError()
            } finally {
                socketManager.close()
            }
        }
    }
}
