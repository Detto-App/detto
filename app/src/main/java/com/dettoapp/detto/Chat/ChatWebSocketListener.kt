package com.dettoapp.detto.Chat

import com.dettoapp.detto.Chat.ChatServiceProvider.Companion.NORMAL_CLOSURE_STATUS
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatWebSocketListener : WebSocketListener() {

    val chatFlow = MutableSharedFlow<String>()

    override fun onOpen(webSocket: WebSocket, response: Response) {
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        GlobalScope.launch {
            chatFlow.emit(text)
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {

        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        GlobalScope.launch {
            chatFlow.emit("Error" + t.localizedMessage)
        }
    }
}

//        GlobalScope.launch {
//            socketEventChannel.emit("Closing...")
//        }
//socketEventChannel.close()
//        webSocket.send("Hi")
//        webSocket.send("Hi again")
//        webSocket.send("Hi again again")
//        webSocket.send("Hi again again again")