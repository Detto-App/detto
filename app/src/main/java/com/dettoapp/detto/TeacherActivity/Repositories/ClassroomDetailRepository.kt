package com.dettoapp.detto.TeacherActivity.Repositories

import com.dettoapp.detto.Models.ClassRoomStudents
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility

class ClassroomDetailRepository {

    suspend fun getClassroomStudents(classID: String): ClassRoomStudents {
        return RetrofitInstance.createClassroomAPI.getClassroomStudents(classID, Utility.TOKEN).body()
                ?: throw Exception("Unable to Fetch Exception")
    }
}