package com.dettoapp.detto.TeacherActivity.Repositories

import com.dettoapp.detto.Models.ClassRoomStudents
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.UtilityClasses.BaseRepository
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility

class ClassroomDetailRepository:BaseRepository() {

    suspend fun getClassroomStudents(classID: String): ClassRoomStudents {
        return RetrofitInstance.createClassroomAPI.getClassroomStudents(classID, Utility.TOKEN).body()
                ?: throw Exception("Unable to Fetch Exception")
    }

    suspend fun getProjects(classID: String): List<ProjectModel> {
        return RetrofitInstance.projectAPI.getProjects(classID,Utility.TOKEN).body()
            ?: throw Exception("Unable to Fetch Project")
    }
}