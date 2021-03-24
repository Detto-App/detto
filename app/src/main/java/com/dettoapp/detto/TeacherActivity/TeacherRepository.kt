package com.dettoapp.detto.TeacherActivity

import android.content.Context
import android.util.Log
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Db.ClassroomDAO
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility

class TeacherRepository(private val dao: ClassroomDAO) {
    suspend fun createClassroom(context: Context,classroom: Classroom){
        Log.d("qwsa","yay")
        RetrofitInstance.createClassroomAPI.createClassroom(classroom,"Bearer "+Utility.gettoken(context))
    }
    fun getUid(context: Context):String{
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        val uid=sharedPreference.getString(Constants.USER_ID,"id")!!
        return uid

    }
    fun getTeacherName(context: Context):String{
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
            ?: throw Exception("Data Storage Exception")
        val tName=sharedPreference.getString(Constants.USER_NAME_KEY,"tname")!!
        return tName
    }
    suspend fun insertClassroom(classroom: Classroom){
        dao.insertClassroom(classroom)
    }

    fun getAllClassRooms() = dao.getAllClassRooms()
}