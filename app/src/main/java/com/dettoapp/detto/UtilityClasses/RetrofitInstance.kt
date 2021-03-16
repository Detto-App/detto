package com.dettoapp.detto.UtilityClasses

import com.dettoapp.detto.APIs.DettoAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance
{
    lateinit var URL : String
    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val dettoAPI : DettoAPI by lazy {
        URL = Constants.BASE_DETTO_URL
        retrofit.create(DettoAPI::class.java)
    }
}