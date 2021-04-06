package com.dettoapp.detto


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

class WebServicesProvider {

    private var _webSocket: WebSocket? = null

    private val socketOkHttpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(39, TimeUnit.SECONDS)
        .hostnameVerifier { _, _ -> true }
        .build()


    private var _webSocketListener: ChatWebSocketListener? = null


    fun startSocket(url: String): Flow<String> =
        with(ChatWebSocketListener()) {
            startSocket(this, url)
            this@with.chatFlow
        }


    private fun startSocket(chatWebSocketListener: ChatWebSocketListener, url: String) {
        _webSocketListener = chatWebSocketListener
        _webSocket = socketOkHttpClient.newWebSocket(
            Request.Builder().url(url).build(),
            chatWebSocketListener
        )
        socketOkHttpClient.dispatcher.executorService.shutdown()
    }


    fun stopSocket() {
        try {
            _webSocket?.close(NORMAL_CLOSURE_STATUS, null)
            _webSocket = null
//            _webSocketListener?.socketEventChannel?.close()
            _webSocketListener = null
        } catch (ex: Exception) {
        }
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
    }

}