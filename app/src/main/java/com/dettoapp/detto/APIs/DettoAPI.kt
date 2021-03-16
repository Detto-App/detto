package com.dettoapp.detto.APIs


import com.dettoapp.detto.Models.User
import retrofit2.Response
import retrofit2.http.GET

interface DettoAPI
{
    @GET("/data")
    suspend fun getGetUsers():Response<ArrayList<User>>
}