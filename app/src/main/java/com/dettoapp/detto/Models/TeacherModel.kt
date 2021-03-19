package com.dettoapp.detto.Models

data class TeacherModel(override val name: String, override val email: String, override val uid: String) : User(name, email, uid)