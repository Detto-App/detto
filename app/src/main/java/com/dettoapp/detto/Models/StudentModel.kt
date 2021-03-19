package com.dettoapp.detto.Models

data class StudentModel(override val name: String, override val email: String, override val uid: String, val sUSN: String)
    : User(name, email, uid)
