package com.dettoapp.detto.StudentActivity

import com.dettoapp.detto.Db.ClassroomDAO

class StudentRepository(private val dao:ClassroomDAO) {
    fun getAllClassRooms() = dao.getAllClassRooms()

}