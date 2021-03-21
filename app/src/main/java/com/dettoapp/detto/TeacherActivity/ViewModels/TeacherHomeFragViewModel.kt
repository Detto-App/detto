package com.dettoapp.detto.TeacherActivity.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.TeacherActivity.TeacherRepository
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TeacherHomeFragViewModel(private val repository: TeacherRepository, private val context: Context) : ViewModel() {

    private val _teacher = MutableLiveData<Resource<String>>()
    val teacher: LiveData<Resource<String>>
        get() = _teacher


    fun classRoomData(classroomName: String, sem: String, sec: String) {
        val uid=repository.getUid(context)
        val classroom = Classroom(classroomName, sem, sec, Utility.createID(),uid)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _teacher.postValue(Resource.Loading())
                repository.createClassroom(context,classroom)
                _teacher.postValue(Resource.Success(data = "classroom created successfully"))
            }catch (e:Exception ){
                Log.d("teachermodel",e.localizedMessage)
                _teacher.postValue(Resource.Error(message = e.localizedMessage))
            }
//            try {
//                RetrofitInstance.createClassroomAPI.createClassroom(classroom)
//            } catch (e: Exception) { }
        }
    }
}