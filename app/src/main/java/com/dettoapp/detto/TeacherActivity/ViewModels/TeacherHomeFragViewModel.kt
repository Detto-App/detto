package com.dettoapp.detto.TeacherActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.TeacherModel
import com.dettoapp.detto.TeacherActivity.TeacherRepository
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

@SuppressLint("StaticFieldLeak")
class TeacherHomeFragViewModel(
        private val repository: TeacherRepository,
        private val context: Context
) : ViewModel() {

    private val _classRoomCreation = MutableLiveData<Resource<String>>()
    val classRoomCreation: LiveData<Resource<String>>
        get() = _classRoomCreation

    private val _classRoomDeletion = MutableLiveData<Resource<String>>()
    val classRoomDeletion: LiveData<Resource<String>>
        get() = _classRoomDeletion


    val allClassRooms = repository.getAllClassRooms()


    fun classRoomData(classroomName: String, sem: String, sec: String, tname: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _classRoomCreation.postValue(Resource.Loading())
                val uid = repository.getUid(context)
                val classroom = Classroom(
                        classroomName,
                        sem,
                        sec,
                        Utility.createID(),
                        repository.getTeacherModel(context)
                )
                repository.insertClassroom(classroom)
                repository.createClassroom(context, classroom)
                _classRoomCreation.postValue(Resource.Success(data = "classroom created successfully"))
            } catch (e: Exception) {
                _classRoomCreation.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }


    fun deleteClassroom(classroom: Classroom) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _classRoomDeletion.postValue(Resource.Loading())
                repository.deleteClassroom(context, classroom)
                _classRoomDeletion.postValue(Resource.Success(data = ""))
            } catch (e: Exception) {
                _classRoomDeletion.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun getTeacherName(): String {
        return repository.getTeacherName(context)
    }
}