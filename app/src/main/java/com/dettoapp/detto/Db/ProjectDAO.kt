package com.dettoapp.detto.Db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dettoapp.detto.Models.ProjectModel

@Dao
interface ProjectDAO {
    @Insert
    suspend fun insertProject(projectModel: ProjectModel)

    @Insert
    suspend fun insertProject(listOfProjectModel: List<ProjectModel>)

    @Query("SELECT * FROM project_table WHERE cid = :cid")
    suspend fun getProject(cid: String): ProjectModel?

    @Query("SELECT * FROM project_table WHERE pid = :pid")
    suspend fun getProjectUsingPid(pid: String): ProjectModel?

    @Query("SELECT * FROM project_table where cid = :cid")
    suspend fun getLocalProject(cid: String) : ProjectModel?

    @Update
    suspend fun updateProject(projectModel: ProjectModel)


}