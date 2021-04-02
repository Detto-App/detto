package com.dettoapp.detto.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dettoapp.detto.UtilityClasses.Constants.PROJECT_PENDING

@Entity(tableName = "project_table")
data class ProjectModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="pid")
    val pid:String,
    @ColumnInfo(name="title")
    val title:String,
    @ColumnInfo(name="desc")
    val desc:String,
    @ColumnInfo(name="studentList")
    val studentList: HashMap<Int,String> =HashMap(),
    @ColumnInfo(name="tid")
    val tid:String,
    @ColumnInfo(name="cid")
    val cid:String,
    @ColumnInfo(name="status")
    val status:String =PROJECT_PENDING
)