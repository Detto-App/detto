package com.dettoapp.detto.Db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dettoapp.detto.Models.ChatMessageLocalStoreModel

@Dao
interface ChatMessageDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChatMessage(chatMessageLocalStoreModel: ChatMessageLocalStoreModel)

    @Query("SELECT * FROM chat_messages WHERE chatroomID = :chatRoomID ORDER BY time")
    fun getChatMessages(chatRoomID:String) : LiveData<List<ChatMessageLocalStoreModel>>
}