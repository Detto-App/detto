package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.Classroom
import retrofit2.Response
import retrofit2.http.*



interface CreateClassroomAPI {
    @POST("/createClassroom")

    suspend fun createClassroom(@Body classroom: Classroom, @Header("Authorization") token: String)

    @GET("/getClassroom/{id}")
    suspend fun getClassroom(@Path(value = "id")id:String, @Header("Authorization") token: String): Response<Classroom>


}