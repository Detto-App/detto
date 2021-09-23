package com.dettoapp.detto.clean_architecture.common

import android.content.Context
import android.content.SharedPreferences

abstract class PreferenceManager(protected val context: Context)
{
    abstract val sharedPreference:SharedPreferences

    class UserDetailsSharedPreference(context: Context) : PreferenceManager(context)
    {
        override val sharedPreference: SharedPreferences
            get() = context.getSharedPreferences(Constants2.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")

        fun getUserRole() = sharedPreference.getInt(Constants2.PARAM_ROLE,-1)
    }
}