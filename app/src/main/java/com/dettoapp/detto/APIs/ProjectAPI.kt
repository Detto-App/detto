package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.ProjectModel
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ProjectAPI {
    @POST("/registerProject")
    suspend fun createProject(@Body projectModel: ProjectModel, @Header("Authorization") token: String)

}