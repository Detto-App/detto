package com.dettoapp.detto.Chat

import android.util.Log
import com.dettoapp.detto.UtilityClasses.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

class ChatServiceProvider {

    private var _webSocket: WebSocket? = null
    private lateinit var urlLink: String


    private val socketOkHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(39, TimeUnit.SECONDS)
            .hostnameVerifier { _, _ -> true }
            .build()


    private var _webSocketListener: ChatWebSocketListener? = null


    fun startSocket(url: String): Flow<Resource<String>> =
            with(ChatWebSocketListener()) {
                startSocket(this, url)
                urlLink = url
                this@with.chatFlow
            }


    private fun startSocket(chatWebSocketListener: ChatWebSocketListener, url: String) {
        Log.d("DDBB","new Socket")
        _webSocketListener = chatWebSocketListener
        _webSocket = socketOkHttpClient.newWebSocket(
                Request.Builder().url(url).build(),
                chatWebSocketListener
        )
        socketOkHttpClient.connectionPool.evictAll()
    }

    fun send(message: String) {
        val sent = _webSocket?.send(message)
        if (sent == null || !sent)
            throw Exception("Not able to Send Message")
    }


    fun stopSocket() {
        _webSocket?.close(NORMAL_CLOSURE_STATUS, null)
        _webSocket = null
        _webSocketListener = null
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
    }
}

//            _webSocketListener?.socketEventChannel?.close()