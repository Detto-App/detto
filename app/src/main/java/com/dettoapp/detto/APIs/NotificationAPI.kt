package com.dettoapp.detto.APIs

import com.dettoapp.detto.Models.PushNotification
import com.dettoapp.detto.UtilityClasses.Constants.CONTENT_TYPE_FCM
import com.dettoapp.detto.UtilityClasses.Constants.SERVER_KEY_FCM
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=$SERVER_KEY_FCM", "Content-Type: $CONTENT_TYPE_FCM")
    @POST("/fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}