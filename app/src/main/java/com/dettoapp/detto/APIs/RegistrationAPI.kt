package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RegistrationAPI {
    @POST("/registerStudent")
    suspend fun sendStudentData(@Body studentModel:StudentModel):Response<Token>


    @POST("/registerTeacher")
    suspend fun sendTeacherData(@Body teacherModel:TeacherModel):Response<Token>


    @GET("/getDetails/{email}")
    suspend fun getDetails(@Path(value = "email")email:String,@Header("role")role:String):Response<ReceivingUserModel>

    @GET("/getTeacherClassrooms/{email}")
    suspend fun getTeacherClassrooms(@Path(value = "email")email: String,@Header("Authorization")token:String):Response<List<Classroom>>

}