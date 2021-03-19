package com.dettoapp.detto.TeacherActivity.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TeacherHomeFragViewModel : ViewModel() {
    fun classRoomData(classroomName: String, sem: String, sec: String) {
        val classroom = Classroom(classroomName, sem, sec, Utility.createID())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                RetrofitInstance.createClassroomAPI.createClassroom(classroom)
            } catch (e: Exception) { }
        }
    }
}