package com.dettoapp.detto.TeacherActivity.Repositories


import com.dettoapp.detto.Models.ClassRoomStudents
import com.dettoapp.detto.UtilityClasses.RetrofitInstance

class ClassroomDetailRepository {

    suspend fun getClassroomStudents(classID: String, token: String): ClassRoomStudents {
        return RetrofitInstance.createClassroomAPI.getClassroomStudents(classID, token).body()
            ?: throw Exception("Unable to Fetch Exception")
    }
}