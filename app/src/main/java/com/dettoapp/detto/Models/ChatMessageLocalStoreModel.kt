package com.dettoapp.detto.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageLocalStoreModel(val chatroomID: String,
                                      val message: String,
                                      val name: String,
                                      val time: Long,
                                      val senderid: String,
                                      @PrimaryKey val chatid: String)