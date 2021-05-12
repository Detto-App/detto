package com.dettoapp.detto.Models

data class Todo(
    val toid:String,
    val tittle:String,
    val category:String ,
    val assigned_to:String,
    val status:Int
)