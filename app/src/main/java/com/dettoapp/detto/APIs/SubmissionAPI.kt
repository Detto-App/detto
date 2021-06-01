package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.githubModels.SubmissionModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SubmissionAPI {
    @POST("/submission/{pid}")
    suspend fun submitUploadedFile(@Body submissionModel: SubmissionModel, @Path(value = "pid") pid: String)

    @GET("/submission/{pid}")
    suspend fun getUploadedFiles(@Path(value = "pid") pid: String) : Response<List<SubmissionModel>>
}