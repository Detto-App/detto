package com.dettoapp.detto.Models

data class TeacherModel(
        val name: String,
        val email: String,
        val uid: String,
        val accessmodelist:ArrayList<AccessModel> = ArrayList()
)