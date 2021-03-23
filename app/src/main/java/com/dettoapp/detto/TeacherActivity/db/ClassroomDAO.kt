package com.dettoapp.detto.TeacherActivity.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ClassroomDAO {
    @Insert
    suspend fun insertClassroom(classroom :Classroom)

    @Query("SELECT * FROM classroom_table")
    fun getAllClassRooms() : LiveData<List<Classroom>>

    //should write update fun if needeed in future

    //and also delete fun
}