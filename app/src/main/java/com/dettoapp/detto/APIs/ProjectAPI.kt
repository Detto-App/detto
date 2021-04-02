package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.ProjectModel
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ProjectAPI {
    @POST("/registerProject/{susn}")
    suspend fun createProject(
        @Body projectModel: ProjectModel,
        @Path(value = "susn") susn: String,
        @Header("Authorization") token: String
    )

}