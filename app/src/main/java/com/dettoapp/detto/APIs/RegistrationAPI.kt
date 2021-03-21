package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RegistrationAPI {
    @POST("/registerStudent")
    suspend fun sendStudentData(@Body studentModel:StudentModel):Response<Token>
    @POST("/registerTeacher")
    suspend fun sendTeacherData(@Body teacherModel:TeacherModel):Response<Token>
    @POST("/getDetails/{email}")
    suspend fun getDetails(@Path(value = "email",encoded = true)email:String):Response<ReceivingUserModel>
}