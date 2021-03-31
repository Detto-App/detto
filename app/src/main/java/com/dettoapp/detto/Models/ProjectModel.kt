package com.dettoapp.detto.Models

data class ProjectModel(
    val pid:String,
    val title:String,
    val desc:String,
    val studentList: HashMap<Int,String> ,
    val tid:String,
    val cid:String
)