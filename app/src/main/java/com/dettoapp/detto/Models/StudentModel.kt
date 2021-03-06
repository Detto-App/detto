package com.dettoapp.detto.Models

data class StudentModel(
    val name: String,
    val email: String,
    val uid: String,
    val susn: String,
    val classrooms: HashSet<String> = HashSet(),
    val projects: HashSet<String> = HashSet()
)
