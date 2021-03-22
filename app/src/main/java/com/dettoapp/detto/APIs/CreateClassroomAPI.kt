package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.Token
import com.dettoapp.detto.Models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CreateClassroomAPI {
    @POST("/createClassroom")

    suspend fun createClassroom(@Body classroom:Classroom, @Header("Authorization") token: String)

}