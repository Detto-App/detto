package com.dettoapp.detto.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "classroom_table")
data class Classroom(
    @ColumnInfo(name="classroom_name")
    val classroomname: String,
    @ColumnInfo(name="sem")
    val sem: String,
    @ColumnInfo(name="section")
    val section: String,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="classroom_uid")
    val classroomuid: String,
    @ColumnInfo(name="user_id")
    val userid: String,
    @ColumnInfo(name="teacher")
    val teacher:TeacherModel
)
