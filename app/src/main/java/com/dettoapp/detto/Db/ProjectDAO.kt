package com.dettoapp.detto.Db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dettoapp.detto.Models.ProjectModel

@Dao
interface ProjectDAO {
    @Insert
    suspend fun insertProject(projectModel : ProjectModel)

    @Query("SELECT * FROM project_table WHERE cid = :cid")
    suspend fun getProject(cid:String) : ProjectModel?
}