package com.dettoapp.detto.Chat

import android.util.Log
import com.dettoapp.detto.Chat.ChatServiceProvider.Companion.NORMAL_CLOSURE_STATUS
import com.dettoapp.detto.UtilityClasses.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatWebSocketListener : WebSocketListener() {

    val chatFlow = MutableSharedFlow<Resource<String>>()

    override fun onOpen(webSocket: WebSocket, response: Response) {
        GlobalScope.launch(Dispatchers.IO) {
            chatFlow.emit(Resource.Confirm(message = ""))
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        GlobalScope.launch(Dispatchers.IO) {
            chatFlow.emit(Resource.Success(text))
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        GlobalScope.launch(Dispatchers.IO) {
            chatFlow.emit(Resource.Error(message = "" + t.localizedMessage))
        }
    }
}