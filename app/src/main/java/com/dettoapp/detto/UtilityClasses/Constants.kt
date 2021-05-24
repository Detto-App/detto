package com.dettoapp.detto.UtilityClasses

import java.text.SimpleDateFormat
import java.util.*

object Constants {

    const val PROGRESS_CHANNEL_ID = "12345"
    const val PROGRESS_CHANNEL_NAME ="Progress Updates"

    const val BASE_DETTO_URL = "https://detto.uk.to/"
    const val USER_EMAIL_KEY = "uEmail"

    const val USER_DETAILS_FILE = "uFile"
    const val PROJECT_CLASS_FILE = "pcFile"
    const val USER_USN_KEY = "usn"
    const val USER_NAME_KEY = "name"
    const val TEACHER = 0
    const val USER_ROLE_KEY = "role"
    const val STUDENT = 1
    const val USER_ID = "uid"
    const val USER_TOKEN_KEY = "token"

    const val TYPE_CID = "cid"
    const val TYPE_PID = "pid"
    const val MESSAGE_LOADING = "Loading..."
    const val ERROR_FILL_ALL_FIELDS = "Enter All Fields"

    val classDetailFragTabNames = arrayOf("Projects", "Students","Deadlines")
    val studentClassDetailFragTabNames = arrayOf("Deadlines","Submission","Todo")

    const val PROJECT_CREATED = 0
    const val PROJECT_NOT_CREATED = -1

    const val PROJECT_PENDING = "pending"
    const val PROJECT_ACCEPTED = "accepted"
    const val PROJECT_REJECTED = "rejected"
    const val ENTIRE_MODEL_KEY = "model"

    const val SHOULD_FETCH="should_fetch"
    const val MANUAL="Manual"
    const val AUTO="Auto"
    const val CHAT_BASE_URL = "wss://detto.uk.to/chat/"

    const val CHAT_DISCONNECTED ="Disconnected"

    const val BASE_URL_FCM = "https://fcm.googleapis.com/"
    const val SERVER_KEY_FCM = "AAAAb6g6I2A:APA91bFnVgQMRH9SUoJcF_MHmi5LaQdqmLK5KWCyeQzX_SJMAEDwjdE7BSINkendBSHQvbZFJNo9LXe-WJHfRHeer1e3-2n23SPPAmtf4WmZ9ZY9VGruzgS4Cdt0o-M6VrqQs7DMvqp5"
    const val CONTENT_TYPE_FCM = "application/json"

    const val DONE =0
    const val NOTDONE =1

    fun Date.toFormattedString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
}