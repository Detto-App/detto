package com.dettoapp.detto.Chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.ChatMessage
import com.dettoapp.detto.UtilityClasses.BaseViewModel
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatViewModel(private val repository: ChatRepository) : BaseViewModel() {

    private val _chatMessages = MutableLiveData<Resource<ArrayList<ChatMessage>>>()
    val chatMessages: LiveData<Resource<ArrayList<ChatMessage>>>
        get() = _chatMessages

    init {

        subscribeToSocketEvents()
        //sendMessage()
    }

    private fun subscribeToSocketEvents() {
        lauchSendMessage()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.webServicesProvider.startSocket("wss://detto.uk.to/chat/1234").buffer(10)
                        .collect {
                            //Log.d("DDAA",it)
                            delay(100)
                            addToChatMessagesList(it)
                        }
            } catch (ex: Exception) {
                Log.d("DDAA", "" + ex.localizedMessage)
            }
        }
    }

    private fun lauchSendMessage() {
//        GlobalScope.launch(Dispatchers.IO) {
//            delay(4000)
//            val chatMessage = ChatMessage("Heyyy", "IDK", Calendar.getInstance().time.toString("MMM dd HH:mm a"),
//                    Utility.STUDENT.uid, Utility.createID())
//
//            val list: ArrayList<ChatMessage> = chatMessages.value?.data
//                    ?: arrayListOf()
//
//            val newList = ArrayList(list)
//            newList.add(chatMessage)
//
//            _chatMessages.postValue(Resource.Success(data = newList))
//        }
    }

    private  fun addToChatMessagesList(message: String,sending :Boolean = false) {

        val senderId = if(!sending)
            Utility.createID()
        else
            Utility.STUDENT.uid

        val chatMessage = ChatMessage(message, "IDK", Calendar.getInstance().time.toString("MMM dd HH:mm a"),
                senderId, Utility.createID())
        val list: ArrayList<ChatMessage> = _chatMessages.value?.data
                ?: arrayListOf()

        val newList = ArrayList(list)
        newList.add(chatMessage)

        _chatMessages.postValue(Resource.Success(data = newList))
        Log.d("DDAA", "List size" + list.size)
    }


    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }


    fun sendMessage(message: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.webServicesProvider.send(message)
            addToChatMessagesList(message,true)
        }
    }

    fun closeConnection() {
        repository.webServicesProvider.stopSocket()
    }
}