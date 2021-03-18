package com.dettoapp.detto.Models

data class TeacherModel(val tName:String,val tEmail:String,val tUid: String) : User(tName,tEmail,tUid) {
}