package com.example.myapplication.network

import android.util.Log
import com.example.myapplication.entity.TestRequest
import com.example.myapplication.entity.TestResponse
import kotlinx.serialization.json.Json
import java.io.DataInputStream
import java.io.OutputStream
import java.net.Socket
import java.nio.ByteBuffer

class SocketManager(
    private val address: String,
    private val port: Int
) {

    companion object {
        private const val TAG = "SocketManager"
    }

    private var socket: Socket? = null
    private var inputStream: DataInputStream? = null
    private var outputStream: OutputStream? = null

    fun connect(): Boolean {
        val s = Socket(address, port)

        Log.d(TAG, "connected: ${s.isConnected}")

        s.soTimeout = 5000
        socket = s
        inputStream = DataInputStream(s.getInputStream())
        outputStream = s.getOutputStream()

        return s.isConnected
    }

    fun send(request: TestRequest) {
        val outputStream = outputStream ?: throw IllegalStateException("outputStream is null!")

        val message = Json.encodeToString(request)

        Log.i(TAG, "sending: $message")

        val messageBytes = message.toByteArray()
        val lengthBytes = ByteBuffer.allocate(4).putInt(messageBytes.size).array()

        outputStream.write(lengthBytes)
        outputStream.write(messageBytes)
        outputStream.flush()
    }

    fun receive(): TestResponse {
        val inputStream = inputStream ?: throw IllegalStateException("inputStream is null!")

        val lengthBytes = ByteArray(4)
        inputStream.readFully(lengthBytes)
        val length = ByteBuffer.wrap(lengthBytes).int

        val buffer = ByteArray(length)
        inputStream.readFully(buffer)
        val message = String(buffer, 0, length)

        Log.d(TAG, "received: $message")

        return Json.decodeFromString<TestResponse>(message)
    }

    fun close() {
        inputStream?.close()
        inputStream = null
        outputStream?.close()
        outputStream = null
        socket?.close()
        socket = null
    }
}
