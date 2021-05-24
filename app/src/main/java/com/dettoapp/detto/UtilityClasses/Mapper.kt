package com.dettoapp.detto.UtilityClasses

import android.util.Log
import com.dettoapp.detto.Models.ChatMessage
import com.dettoapp.detto.Models.ChatMessageLocalStoreModel
import com.dettoapp.detto.Models.*
import com.dettoapp.detto.UtilityClasses.Constants.toFormattedString
import java.util.*

object Mapper {

    private val calendar by lazy { Calendar.getInstance() }

    fun mapChatMessageToChatMessageLocalModel(chatRoomID: String, chatMessage: ChatMessage): ChatMessageLocalStoreModel {
        return ChatMessageLocalStoreModel(chatRoomID, chatMessage.message, chatMessage.name, chatMessage.time.toLong(), chatMessage.senderid, chatMessage.chatid)
    }


    fun mapChatMessageLocalModelToChatMessage(chatMessageLocalStoreModel: ChatMessageLocalStoreModel): ChatMessage {
        calendar.timeInMillis = chatMessageLocalStoreModel.time
        val timeInString = calendar.time.toFormattedString("MMM dd hh:mm a")
        return ChatMessage(chatMessageLocalStoreModel.message, chatMessageLocalStoreModel.name, timeInString, chatMessageLocalStoreModel.senderid, chatMessageLocalStoreModel.chatid)
    }

    fun mapProjectModelAndRubricsModelToProjectRubricsModel(usn:String,pid:String,name:String, rubricsModel: RubricsModel): ProjectRubricsModel {
        return ProjectRubricsModel(usn,pid,name,rubricsModel)
    }
}