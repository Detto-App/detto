package com.dettoapp.detto.TeacherActivity

import android.content.Context
import android.util.Log
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.UtilityClasses.RetrofitInstance

class TeacherRepository {
    suspend fun createClassroom(context: Context,classroom: Classroom){
        Log.d("qwsa","yay")
        RetrofitInstance.createClassroomAPI.createClassroom(classroom)
    }
}