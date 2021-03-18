package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.Models.TeacherModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationAPI {
    @POST("/studentRegis")
    suspend fun sendStudentData(@Body studentModel:StudentModel):Response<ResponseBody>
    @POST("/teacherRegis")
    suspend fun sendTeacherData(@Body teacherModel:TeacherModel):Response<ResponseBody>

}