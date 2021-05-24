package com.dettoapp.detto.APIs

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface GDriveAPI
{
    @GET("/gDriveToken")
    suspend fun getGDriveToken():Response<ResponseBody>
}