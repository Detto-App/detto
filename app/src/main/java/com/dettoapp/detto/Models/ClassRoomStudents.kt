package com.dettoapp.detto.Models

data class ClassRoomStudents(
    val classID: String,
    val studentList: HashSet<StudentModel> = HashSet()
)