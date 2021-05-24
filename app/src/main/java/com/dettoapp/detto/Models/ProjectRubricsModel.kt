package com.dettoapp.detto.Models

import androidx.room.PrimaryKey

data class ProjectRubricsModel(
    val usn:String,
    val pid:String,
    val name:String,
    val rubrics:RubricsModel
    )