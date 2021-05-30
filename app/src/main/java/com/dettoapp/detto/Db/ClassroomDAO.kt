package com.dettoapp.detto.Db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dettoapp.detto.Models.Classroom

@Dao
interface ClassroomDAO {
    @Insert
    suspend fun insertClassroom(classroom: Classroom)

    @Query("SELECT * FROM classroom_table")
    fun getAllClassRooms(): LiveData<List<Classroom>>

    @Query("SELECT * FROM classroom_table")
    suspend fun getAllClassRoomList(): List<Classroom>

    @Query("SELECT * FROM classroom_table where classroom_uid = :cid")
    suspend fun getLocalClassroom(cid:String): Classroom?

    @Insert
    suspend fun insertClassroom(classroomList: List<Classroom>)

    @Delete
    suspend fun deleteClassroom(classroom: Classroom)

    //should write update fun if needeed in future

    //and also delete fun
}