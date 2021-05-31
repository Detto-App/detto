package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.*
import okhttp3.ResponseBody
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
            @Path(value = "cid") cid: String,
            @Header("Authorization") token: String
    ): Response<List<ProjectModel>>

    @GET("/getProjectDetails/{pid}")
    suspend fun getSingleProjectDetails(
            @Path(value = "pid") pid: String,
            @Header("Authorization") token: String

    ): Response<ProjectModel>

    @POST("/regStudentToProject/{pid}/{name}/{susn}")
    suspend fun regStudentToProject(
            @Path(value = "pid") pid: String,
            @Path(value = "name") name: String,
            @Path(value = "susn") susn: String,
            @Header("Authorization") token: String
    )

    @GET("/changeStatus/{pid}/{status}")
    suspend fun changeStatus(
            @Path(value = "pid") pid: String,
            @Path(value = "status") status: String,
            @Header("Authorization") token: String
    ): Response<ResponseBody>

    @POST("/updateProject/{pid}")
    suspend fun updateProject(
            @Body projectModel: ProjectModel,
            @Path(value = "pid") pid: String,
            @Header("Authorization") token: String
    )

    @POST("/getManyProjectDetails")
    suspend fun getManyProjectDetails(
            @Body set: HashSet<String>,
            @Header("Authorization") token: String
    ): Response<ArrayList<ProjectModel>>

    @POST("/createDeadline/{cid}")
    suspend fun createDeadline(
            @Body deadlineModel: DeadlineModel,
            @Header("Authorization") token: String,
            @Path(value = "cid") cid: String,

            )

    @GET("/getDeadline/{cid}")
    suspend fun getDeadline(
            @Path(value = "cid") cid: String,
            @Header("Authorization") token: String
    ): Response<ArrayList<DeadlineModel>>

    @POST("/createTodo/{cid}")
    suspend fun createTodo(
            @Body todo: Todo,
            @Header("Authorization") token: String,
            @Path(value = "cid") cid: String
    )

    @GET("/getTodo/{pid}")
    suspend fun getTodo(
            @Path(value = "pid") pid: String,
            @Header("Authorization") token: String
    ): Response<ArrayList<Todo>>

    @GET("/getTimeline/{pid}")
    suspend fun getTimeline(
            @Path(value = "pid") pid: String,
            @Header("Authorization") token: String
    ): Response<ArrayList<Timeline>>


    @GET("/deleteTodo/{pid}/{toid}")
    suspend fun deleteTodo(
            @Path(value = "pid") pid: String,
            @Path(value = "toid") toid: String,
            @Header("Authorization") token: String
    ): Response<ResponseBody>

    @GET("/changeStatusOfTodo/{toid}/{pid}")
    suspend fun changeStatusOfTodo(
            @Path(value = "toid") toid: String,
            @Path(value = "pid") pid: String,
            @Header("Authorization") token: String
    ): Response<ResponseBody>

    @POST("/getStudentNameList")
    suspend fun getStudentNameList(
            @Body usnMapSet: HashSet<String>,
            @Header("Authorization") token: String
    ): Response<HashMap<String, String>>

    @POST("/createProjects/{cid}")
    suspend fun createProjects(
            @Body projectModelList: ArrayList<ProjectModel>,
            @Path(value = "cid") cid: String,
            @Header("Authorization") token: String
    ): Response<ResponseBody>

    @GET("/getStudentModel/{susn}")
    suspend fun getStudentModel(
            @Path(value = "susn") susn: String,
            @Header("Authorization") token: String
    ): Response<StudentModel>
}