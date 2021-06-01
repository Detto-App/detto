package com.dettoapp.detto.StudentActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.BaseViewModel
import com.dettoapp.detto.UtilityClasses.Resource
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class StudentHomeFragViewModel(private val repository: StudentRepository, private val context: Context) :
    BaseViewModel() {
    private val _project1 = MutableLiveData<Resource<String>>()
    val project1: LiveData<Resource<String>>
        get() = _project1

    private val _allClassRooms = MutableLiveData<Resource<List<Classroom>>>()
    val allClassRooms: LiveData<Resource<List<Classroom>>>
        get() = _allClassRooms

    init {
        initialize()
        getAllClassRooms()
    }

    private fun getAllClassRooms() {
        operateWithLiveData(_allClassRooms, mainFunction = {
            val list = repository.getClassRoomList()
            list.forEach {
                Firebase.messaging.subscribeToTopic("/topics/${it.classroomuid}")
            }
            it.postValue(Resource.Success(data = list))
        })
    }

    private fun initialize() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (repository.getShouldFetch(context)) {
                    _project1.postValue(Resource.Confirm(message = ""))
                }
            } catch (e: Exception) {
                _project1.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun download() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _project1.postValue(Resource.Loading())
                repository.shouldFetch(context)
                _project1.postValue(Resource.Success(data = ""))

            } catch (e: Exception) {
                _project1.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }
}