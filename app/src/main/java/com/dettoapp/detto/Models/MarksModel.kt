package com.dettoapp.detto.Models

data class MarksModel (
    val title:String,
    val maxMarks:Int,
    val convertTo:Int,
    var marks:Double?=0.0
)
