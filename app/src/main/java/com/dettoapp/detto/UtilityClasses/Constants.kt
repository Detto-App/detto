package com.dettoapp.detto.UtilityClasses

object Constants {
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
    const val TYPE_PID ="pid"
    const val MESSAGE_LOADING = "Loading..."
    const val ERROR_FILL_ALL_FIELDS = "Enter All Fields"

    val classDetailFragTabNames = arrayOf("Projects", "Students","Deadlines")

    const val PROJECT_CREATED = 0
    const val PROJECT_NOT_CREATED = -1

    const val PROJECT_PENDING = "pending"
    const val PROJECT_ACCEPTED = "accepted"
    const val PROJECT_REJECTED = "rejected"
    const val ENTIRE_MODEL_KEY = "model"

    const val SHOULD_FETCH="should_fetch"
    const val MANUAL="Manual"
    const val AUTO="Auto"
}