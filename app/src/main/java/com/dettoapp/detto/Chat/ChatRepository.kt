package com.dettoapp.detto.Chat

import com.dettoapp.detto.Db.ChatMessageDAO
import com.dettoapp.detto.Models.ChatMessage
import com.dettoapp.detto.UtilityClasses.BaseRepository
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Mapper
import com.google.gson.Gson
import kotlinx.coroutines.flow.buffer

class ChatRepository(private val webServicesProvider: ChatServiceProvider, private val chatMessageDAO: ChatMessageDAO) : BaseRepository() {

    @Suppress("SpellCheckingInspection")
    private val gson = Gson()

    fun getChatMessagesFromServer(endPoint: String) = webServicesProvider
            .startSocket(Constants.CHAT_BASE_URL + endPoint)
            .buffer(10)

    fun endConnection() = webServicesProvider.stopSocket()

    private fun sendMessageToServer(message: String) = webServicesProvider.send(message)

    suspend fun sendMessage(chatRoomID: String, message: ChatMessage) {
        sendMessageToServer(gson.toJson(message))
        chatMessageDAO.insertChatMessage(Mapper.mapChatMessageToChatMessageLocalModel(chatRoomID, message))
    }

    suspend fun storeMessage(chatRoomID: String, message: ChatMessage) {
        chatMessageDAO.insertChatMessage(Mapper.mapChatMessageToChatMessageLocalModel(chatRoomID, message))
    }

    fun getChatMessagesFromLocalDbFlow(chatRoomID: String) = chatMessageDAO.getChatMessagesFlow(chatRoomID)
}