package com.dettoapp.detto.TeacherActivity.Repositories

import android.content.Context
import android.util.Log
import com.dettoapp.detto.Db.ClassroomDAO
import com.dettoapp.detto.Models.AccessModel
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ClassroomSettingsModel
import com.dettoapp.detto.Models.TeacherModel
import com.dettoapp.detto.UtilityClasses.BaseRepository
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import okhttp3.ResponseBody

class TeacherRepository(private val dao: ClassroomDAO) : BaseRepository() {
    suspend fun createClassroom(classroom: Classroom) {
        RetrofitInstance.createClassroomAPI.createClassroom(classroom, Utility.TOKEN)
    }

    fun getTeacherName(): String {
        return Utility.TEACHER.name
    }

    suspend fun insertClassroom(classroom: Classroom) {
        dao.insertClassroom(classroom)
    }

    fun getAllClassRooms() = dao.getAllClassRooms()

    fun getTeacherModel(): TeacherModel {
        return Utility.TEACHER
    }
    suspend fun getTeacherModelFromServer(tid: String):TeacherModel{
        return RetrofitInstance.createClassroomAPI.getTeacherModel(tid,Utility.TOKEN)
    }

    fun getClassroomSettingsModel(teamSize: String, projectType: String, groupType: String): ClassroomSettingsModel {
        return ClassroomSettingsModel(teamSize, projectType, groupType)
    }

    suspend fun deleteClassroom(classroom: Classroom) {
        RetrofitInstance.createClassroomAPI.deleteClassroom(classroom.classroomuid, Utility.TOKEN)
        dao.deleteClassroom(classroom)
    }

    suspend fun addAccess(context: Context,accessModel:AccessModel,tid:String){
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
            ?: throw Exception("Data Storage Exception")
        with(sharedPreference.edit())
        {
            putString(Constants.ACCESS, accessModel.type + " " + accessModel.sem)
            apply()
        }

        Log.d("QQA","ghg")

        RetrofitInstance.createClassroomAPI.addAccess(accessModel,tid,Utility.TOKEN)

    }
}