package com.dettoapp.detto.Models

data class ReceivingUserModel(val teacher: TeacherModel? = null, val student: StudentModel? = null, val token: String)