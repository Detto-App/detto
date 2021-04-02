package com.dettoapp.detto.TeacherActivity.Repositories

import com.dettoapp.detto.Db.ClassroomDAO
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ClassroomSettingsModel
import com.dettoapp.detto.Models.TeacherModel
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility

class TeacherRepository(private val dao: ClassroomDAO) {
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

    fun getClassroomSettingsModel(teamSize: String, projectType: String): ClassroomSettingsModel {
        return ClassroomSettingsModel(teamSize, projectType)
    }

    suspend fun deleteClassroom(classroom: Classroom) {
        RetrofitInstance.createClassroomAPI.deleteClassroom(classroom.classroomuid, Utility.TOKEN)
        dao.deleteClassroom(classroom)
    }
}