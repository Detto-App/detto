package com.dettoapp.detto.TeacherActivity.db

class ClassroomRepository(private val dao:ClassroomDAO) {
    suspend fun insert(classroom: Classroom){
        dao.insertclassroom(classroom)
    }
}