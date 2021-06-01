package com.dettoapp.detto.UtilityClasses

import com.dettoapp.detto.Models.*
import com.dettoapp.detto.UtilityClasses.Constants.toFormattedString
import java.util.*
import kotlin.collections.HashMap

object Mapper {

    private val calendar by lazy { Calendar.getInstance() }

    fun mapChatMessageToChatMessageLocalModel(
        chatRoomID: String,
        chatMessage: ChatMessage
    ): ChatMessageLocalStoreModel {
        return ChatMessageLocalStoreModel(
            chatRoomID,
            chatMessage.message,
            chatMessage.name,
            chatMessage.time.toLong(),
            chatMessage.senderid,
            chatMessage.chatid
        )
    }


    fun mapChatMessageLocalModelToChatMessage(chatMessageLocalStoreModel: ChatMessageLocalStoreModel): ChatMessage {
        calendar.timeInMillis = chatMessageLocalStoreModel.time
        val timeInString = calendar.time.toFormattedString("MMM dd hh:mm a")
        return ChatMessage(
            chatMessageLocalStoreModel.message,
            chatMessageLocalStoreModel.name,
            timeInString,
            chatMessageLocalStoreModel.senderid,
            chatMessageLocalStoreModel.chatid
        )
    }

    fun mapProjectModelAndRubricsModelToProjectRubricsModel(
        usn: String,
        pid: String,
        name: String,
        rubricsModel: RubricsModel
    ): ProjectRubricsModel {
        return ProjectRubricsModel(usn, pid, name, rubricsModel)
    }

    fun mapRubricsModelToTempRubricsModel(
        rid: String,
        titleMarksList: ArrayList<MarksModel>,
        cid: String
    ): RubricsModel {
        return RubricsModel(rid, titleMarksList, cid)
    }

    fun mapProjectModel(
        pid: String,
        title: String? = "Please Enter your Project Title",
        desc: String? = "Enter your Project Description",
        studentUsnlist: HashSet<String>,
        tid: String,
        cid: String,
        studentNameList: ArrayList<String>,
        projectStudentList:HashMap<String,String>
    ): ProjectModel {
        return ProjectModel(pid, title!!, desc!!, studentUsnlist, tid, cid, studentNameList = studentNameList,projectStudentList = projectStudentList)
    }
}