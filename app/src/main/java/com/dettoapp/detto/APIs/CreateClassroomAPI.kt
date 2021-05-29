package com.dettoapp.detto.APIs

import androidx.room.Update
import com.dettoapp.detto.Models.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface CreateClassroomAPI {
    @POST("/createClassroom")
    suspend fun createClassroom(@Body classroom: Classroom, @Header("Authorization") token: String)

    @GET("/getClassroom/{id}")
    suspend fun getClassroom(
        @Path(value = "id") id: String,
        @Header("Authorization") token: String
    ): Response<Classroom>

    @GET("/deleteClassroom/{cid}")
    suspend fun deleteClassroom(
        @Path(value = "cid") cid: String,
        @Header("Authorization") token: String
    )

    @POST("/regStudentToClassroom/{cid}")
    suspend fun regStudentToClassroom(
        @Body studentClassroom: StudentModel,
        @Path(value = "cid") cid: String,
        @Header("Authorization") token: String
    )

    @GET("/getStudentClassroom/{semail}")
    suspend fun getStudentClassroom(
        @Path(value = "semail") semail: String,
        @Header("Authorization") token: String
    ): Response<ArrayList<Classroom>>

    @GET("/getClassStudents/{classID}")
    suspend fun getClassroomStudents(
        @Path(value = "classID") classID: String,
        @Header("Authorization") token: String
    ): Response<ClassRoomStudents>

    @POST("/createRubrics")
    suspend fun createRubrics(@Body rubricsModel: RubricsModel, @Header("Authorization") token: String)

    @GET("getRubrics/{classID}")
    suspend fun getRubrics(
        @Path(value = "classID") classID: String,
        @Header("Authorization") token: String
    ): Response<RubricsModel>

    @GET("getProjectRubrics/{cid}/{pid}")
    suspend fun getRubricsForProject(
        @Path(value = "cid") cid: String,
        @Path(value = "pid") pid: String,
        @Header("Authorization") token: String
    ): Response<ArrayList<ProjectRubricsModel>>

    @POST("insertProjectRubrics")
    suspend fun insertProjectRubrics(
        @Body projectRubricsList: ArrayList<ProjectRubricsModel>,
        @Header("Authorization") token: String
    )

    @POST("updateProjectRubrics/{cid}/{pid}")
    suspend fun updateProjectRubrics(
        @Body studentHashMap: HashMap<String, RubricsModel>,
        @Path("cid") cid: String,
        @Path("pid") pid: String,
        @Header("Authorization") token: String
    )

    @POST("addAccess/{tid}")
    suspend fun addAccess(
        @Body accessModel: AccessModel,
        @Path(value = "tid") tid: String,
        @Header("Authorization") token: String
    ): Response<ResponseBody>
}