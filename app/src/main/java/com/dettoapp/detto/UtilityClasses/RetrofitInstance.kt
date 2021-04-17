package com.dettoapp.detto.UtilityClasses

import com.dettoapp.detto.APIs.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(Constants.BASE_DETTO_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        private val retrofit2 by lazy {
            Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_FCM)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        val dettoAPI: DettoAPI by lazy {
            retrofit.create(DettoAPI::class.java)
        }
        val createClassroomAPI: CreateClassroomAPI by lazy {
            retrofit.create(CreateClassroomAPI::class.java)
        }

        val registrationAPI: RegistrationAPI by lazy {
            retrofit.create(RegistrationAPI::class.java)
        }

        val projectAPI: ProjectAPI by lazy {
            retrofit.create(ProjectAPI::class.java)
        }

        val notificationAPI: NotificationAPI by lazy {
            retrofit2.create(NotificationAPI::class.java)
        }
    }

}