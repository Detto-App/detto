package com.dettoapp.detto.TeacherActivity.db

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ClassroomDAO {
    @Insert
    suspend fun insertclassroom(classroom :Classroom)

    //should write update fun if needeed in future

    //and also delete fun
}