package com.dettoapp.detto.LinkParseActivity

import android.content.Context
import com.dettoapp.detto.Db.ClassroomDAO
//import com.dettoapp.detto.Db.StudentClassroomDAO
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.UtilityClasses.Constants

class LinkParserRepository(private val dao: ClassroomDAO) {

    fun getRole(context: Context): Int {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
            ?: throw Exception("Data Storage Exception")
        return sharedPreference.getInt(Constants.USER_ROLE_KEY, -1)
    }
    suspend fun insertClassroom(classroom: Classroom){
        dao.insertClassroom(classroom)
    }
}