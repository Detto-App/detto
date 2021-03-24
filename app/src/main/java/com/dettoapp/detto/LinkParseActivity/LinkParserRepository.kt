package com.dettoapp.detto.LinkParseActivity

import android.content.Context
import com.dettoapp.detto.UtilityClasses.Constants

class LinkParserRepository {

    fun getRole(context: Context): Int {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
            ?: throw Exception("Data Storage Exception")
        return sharedPreference.getInt("role", -1)

    }
}