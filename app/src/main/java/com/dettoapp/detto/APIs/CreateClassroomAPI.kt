package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.StudentModel
import retrofit2.Response
import retrofit2.http.*



interface CreateClassroomAPI {
    @POST("/createClassroom")
    suspend fun createClassroom(@Body classroom: Classroom, @Header("Authorization") token: String)

    @GET("/getClassroom/{id}")
    suspend fun getClassroom(@Path(value = "id")id:String, @Header("Authorization") token: String): Response<Classroom>

    @GET("/deleteClassroom/{cid}")
    suspend fun deleteClassroom(@Path(value = "cid")cid:String,@Header("Authorization")token: String)

    @POST("/regStudentToClassroom/{cid}")
    suspend fun regStudentToClassroom(@Body studentClassroom:StudentModel,@Path(value = "cid")cid:String, @Header("Authorization") token: String)

    @GET("/getStudentClassroom/{semail}")
    suspend fun getStudentClassroom(@Path(value = "semail")semail:String,
                                    @Header("Authorization")token: String): Response<ArrayList<Classroom>>
}