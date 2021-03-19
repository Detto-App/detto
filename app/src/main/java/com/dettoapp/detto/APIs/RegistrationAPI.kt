package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.ReceivingUserModel
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.Models.TeacherModel
import com.dettoapp.detto.Models.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RegistrationAPI {
    @POST("/studentRegis")
    suspend fun sendStudentData(@Body studentModel:StudentModel):Response<ResponseBody>
    @POST("/teacherRegis")
    suspend fun sendTeacherData(@Body teacherModel:TeacherModel):Response<ResponseBody>
    @POST("/getDetails/{email}")
    suspend fun getDetails(@Path(value = "email",encoded = true)email:String):Response<ReceivingUserModel>
}