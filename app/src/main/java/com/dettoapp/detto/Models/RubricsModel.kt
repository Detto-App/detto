package com.dettoapp.detto.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rubrics_table")
data class RubricsModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "rid")
    val rid: String,
    @ColumnInfo(name = "titleMarksList")
    var titleMarksList: ArrayList<MarksModel>,
    @ColumnInfo(name = "cid")
    val cid: String
)