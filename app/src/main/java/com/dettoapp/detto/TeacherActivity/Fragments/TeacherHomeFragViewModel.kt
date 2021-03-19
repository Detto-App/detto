package com.dettoapp.detto.TeacherActivity.Fragments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TeacherHomeFragViewModel:ViewModel() {
    fun classRoomData(classroomname:String,sem:String,sec:String) {
//        Log.d("abcd",sem)
        val classroom=Classroom(classroomname,sem,sec,Utility.createID())
        viewModelScope.launch(Dispatchers.IO) {
            try{
                RetrofitInstance.CREATECLASSROOM.createClassroom(classroom)

            }
            catch (e:Exception){

            }
        }
    }

}