package com.dettoapp.detto.Chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.ChatMessage
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.UtilityClasses.BaseViewModel
import com.dettoapp.detto.UtilityClasses.Constants.toFormattedString
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlin.collections.ArrayList

class ChatViewModel(private val repository: ChatRepository) : BaseViewModel() {

    private val _chatMessages = MutableLiveData<Resource<ArrayList<ChatMessage>>>()
    val chatMessages: LiveData<Resource<ArrayList<ChatMessage>>>
        get() = _chatMessages

    private val _chatMessageEvent = MutableLiveData<Resource<String>>()
    val chatMessageEvent: LiveData<Resource<String>>
        get() = _chatMessageEvent

    private lateinit var chatCollectJob: Job

    private var isFailure: Boolean = false

    private lateinit var classroom: Classroom
    private lateinit var name: String
    private lateinit var userID: String

    fun initialise(localClassroom: Classroom, name: String, userID: String) {
        classroom = localClassroom
        this.name = name
        this.userID = userID
        subscribeToSocketEvents(classroom)

    }

    private fun subscribeToSocketEvents(localClassroom: Classroom) {
        chatCollectJob = viewModelScope.launch(Dispatchers.IO)
        {
            try {
                startSocket()
            } catch (ce: CancellationException) {
                // You can ignore or log this exception
            } catch (ex: Exception) {
                _chatMessageEvent.postValue(Resource.Error(message = "" + ex.localizedMessage))
            }
        }
    }


    private suspend fun startSocket() =
            repository.webServicesProvider.startSocket("wss://detto.uk.to/chat/" + classroom.classroomuid).buffer(10)
                    .collect {
                        when (it) {
                            is Resource.Success -> {
                                isFailure = false
                                delay(100)
                                addToChatMessagesList(it.data!!)
                                _chatMessageEvent.postValue(Resource.Success(data = "Dont Clear"))
                            }
                            is Resource.Error -> {
                                isFailure = true
                            }
                            else -> {
                            }
                        }
                    }

    private fun addToChatMessagesList(message: String, sending: Boolean = false) {

//        val senderId = if (!sending)
//            Utility.createID()
//        else
//            Utility.STUDENT.uid

//        val chatMessage = ChatMessage(message, "IDK", Calendar.getInstance().time.toFormattedString("MMM dd HH:mm a"),
//                senderId, Utility.createID())

        try {

            Log.d("DDBB", " message " + message)
            val type = object : TypeToken<ChatMessage>() {}.type
            val chatMessage = Gson().fromJson<ChatMessage>(message, type)
            val list: ArrayList<ChatMessage> = _chatMessages.value?.data
                    ?: arrayListOf()

            val newList = ArrayList(list)
            newList.add(chatMessage)

            _chatMessages.postValue(Resource.Success(data = newList))
        } catch (e: Exception) {
            Log.d("DDBB", "Parsing " + e.localizedMessage)
        }

    }


    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                if (isFailure) {
                    cancelPreviousJob()
                    subscribeToSocketEvents(classroom)
                    chatCollectJob.join()
                }
                _chatMessageEvent.postValue(Resource.Loading())
                val chatMessage = ChatMessage(message, name, Calendar.getInstance().time.toFormattedString("MMM dd HH:mm a"), userID, Utility.createID())
                val chatMessageString = Gson().toJson(chatMessage)
                repository.webServicesProvider.send(chatMessageString)
                addToChatMessagesList(chatMessageString, true)
                _chatMessageEvent.postValue(Resource.Success(""))
            } catch (e: Exception) {
                Log.d("DDBB ", "HH " + e.localizedMessage)
            }
        }
    }

    private suspend fun cancelPreviousJob() {

        chatCollectJob.let {
            if (it.isActive) {
                repository.webServicesProvider.stopSocket()
                it.cancelAndJoin()
            }
        }
    }

    fun closeConnection() {
        repository.webServicesProvider.stopSocket()
    }
}
