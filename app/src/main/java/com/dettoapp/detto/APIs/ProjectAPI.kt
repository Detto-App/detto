package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.ProjectModel
import retrofit2.Response
import retrofit2.http.*

interface ProjectAPI {

    @POST("/registerProject/{susn}")
    suspend fun createProject(
        @Body projectModel: ProjectModel,
        @Path(value = "susn") susn: String,
        @Header("Authorization") token: String
    )

    @GET("/getProjects/{cid}")
    suspend fun getProjects(
        @Path(value = "cid")cid:String,
        @Header("Authorization")token: String
    ):Response<List<ProjectModel>>

    @GET("/getProjectDetails/{pid}")
    suspend fun getSingleProjectDetails(
        @Path(value="pid")pid:String,
        @Header("Authorization")token: String

    ):Response<ProjectModel>



    @POST("/regStudentToProject/{pid}/{name}/{susn}")
    suspend fun regStudentToProject(
        @Path(value = "pid")pid: String,
        @Path(value = "name")name: String,
        @Path(value = "susn")susn:String,
        @Header("Authorization")token: String
    )

}