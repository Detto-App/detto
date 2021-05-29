package com.dettoapp.detto.Chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.ChatMessage
import com.dettoapp.detto.Models.ChatMessageLocalStoreModel
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.UtilityClasses.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transform

@Suppress("EXPERIMENTAL_API_USAGE")
class ChatViewModel(private val repository: ChatRepository) : BaseViewModel() {

    private lateinit var chatCollectJob: Job

    private val _chatMessageEvent = MutableLiveData<Resource<String>>()
    val chatMessageEvent: LiveData<Resource<String>>
        get() = _chatMessageEvent

    private val chatRoomID = MutableStateFlow("")

    private val chatMessageLocalStoreModelFlow = chatRoomID.flatMapLatest {
        repository.getChatMessagesFromLocalDbFlow(it)
    }.transform { value ->
        emit(transformList(value))
    }

    val chatMessages = chatMessageLocalStoreModelFlow.asLiveData(Dispatchers.Default)

    private var firstTime = true
    private var isFailure: Boolean = false
    private lateinit var classroom: Classroom
    private lateinit var name: String
    private lateinit var userID: String

    fun initialise(localClassroom: Classroom, name: String, userID: String) {
        classroom = localClassroom
        this.name = name
        this.userID = userID
        chatRoomID.value = classroom.classroomuid
        subscribeToSocketEvents()
        Log.d("DDFF", "chat is " + localClassroom.classroomuid)
    }


    private suspend fun transformList(localChatMessageDBList: List<ChatMessageLocalStoreModel>): Resource<ArrayList<ChatMessage>> {
        delayChatDisplayForFirstTime()

        val list = ArrayList<ChatMessage>()
        localChatMessageDBList.forEach {
            list.add(Mapper.mapChatMessageLocalModelToChatMessage(it))
        }
        return Resource.Success(data = list)
    }


    private suspend fun delayChatDisplayForFirstTime() {
        if (firstTime) {
            delay(500)
            firstTime = false
        }
    }


    private fun subscribeToSocketEvents() {
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
        repository.getChatMessagesFromServer(classroom.classroomuid)
            .collect {
                when (it) {
                    is Resource.Success -> {
                        isFailure = false
                        addToChatMessagesList(it.data!!)
                        _chatMessageEvent.postValue(Resource.Success(data = "Don't Clear"))
                    }
                    is Resource.Error -> {
                        isFailure = true
                        _chatMessageEvent.postValue(Resource.Error(message = Constants.CHAT_DISCONNECTED))
                    }

                    is Resource.Confirm -> {
                        isFailure = false
                        _chatMessageEvent.postValue(Resource.Confirm(message = ""))
                    }
                    else -> {
                    }
                }
            }

    private suspend fun addToChatMessagesList(message: String) {
        try {
            val type = object : TypeToken<ChatMessage>() {}.type
            val chatMessage = Gson().fromJson<ChatMessage>(message, type)
            repository.storeMessage(chatRoomID.value, chatMessage)
        } catch (e: Exception) {
            _chatMessageEvent.postValue(Resource.Error(message = "Parsing Error"))
        }
    }


    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                if (message.isEmpty()) {
                    return@launch
                }
                _chatMessageEvent.postValue(Resource.Loading())

                if (isFailure) {
                    throw Exception("Not able to Send Message\nReconnect or Check InternetConnection")
                }

                val chatMessage = ChatMessage(
                    message, name,
                    System.currentTimeMillis().toString(),
                    userID,
                    Utility.createID()
                )

                repository.sendMessage(chatRoomID.value, chatMessage)

                _chatMessageEvent.postValue(Resource.Success(""))
            } catch (e: Exception) {
                _chatMessageEvent.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    private suspend fun reconnectToServer() {
        cancelPreviousJob()
        subscribeToSocketEvents()
        chatCollectJob.join()
    }

    private suspend fun cancelPreviousJob() {
        if (chatCollectJob.isActive) {
            repository.endConnection()
            chatCollectJob.cancelAndJoin()
        }
    }

    fun closeConnection() {
        repository.endConnection()
    }

    fun reconnect() {
        viewModelScope.launch {
            reconnectToServer()
        }
    }
}