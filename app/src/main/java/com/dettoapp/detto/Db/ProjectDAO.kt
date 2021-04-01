package com.dettoapp.detto.Db

import androidx.room.Dao
import androidx.room.Insert
import com.dettoapp.detto.Models.ProjectModel

@Dao
interface ProjectDAO {
    @Insert
    suspend fun insertProject(projectModel : ProjectModel)
}