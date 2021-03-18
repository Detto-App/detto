package com.dettoapp.detto.Models

data class StudentModel(val sName:String,val sEmail:String,val sUid: String,val sUSN:String) :User(sName,sEmail,sUid) {
}