package com.dettoapp.detto.APIs

import com.dettoapp.detto.TeacherActivity.db.Classroom
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CreateClassroomAPI {
    @POST("/createClassroom")

    suspend fun createClassroom(@Body classroom: Classroom, @Header("Authorization") token: String)

}