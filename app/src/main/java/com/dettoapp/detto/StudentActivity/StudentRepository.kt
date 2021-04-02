package com.dettoapp.detto.StudentActivity

import android.content.Context
import com.dettoapp.detto.Db.ClassroomDAO
import com.dettoapp.detto.Db.ProjectDAO
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility

class StudentRepository(private val dao:ClassroomDAO,private val projectDao :ProjectDAO) {
    fun getAllClassRooms() = dao.getAllClassRooms()

    fun storeProjectInSharedPref(classID:String,context:Context)
    {
        val sharedPreference = context.getSharedPreferences(Constants.PROJECT_CLASS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")

        with(sharedPreference.edit())
        {
            putInt(classID,Constants.PROJECT_CREATED)
            apply()
        }
    }

    fun getProjectFromSharedPref(classID: String,context: Context):Int
    {
        val sharedPreference = context.getSharedPreferences(Constants.PROJECT_CLASS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        return sharedPreference.getInt(classID,Constants.PROJECT_NOT_CREATED)
    }
    suspend fun insertProject(projectModel:ProjectModel){
        projectDao.insertProject(projectModel)
    }

    fun getSusn(context: Context):String{
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
            ?: throw Exception("Data Storage Exception")
        return sharedPreference.getString(Constants.USER_USN_KEY,"")!!
    }
    suspend fun insertProjectToServer(projectModel:ProjectModel,context: Context){
        RetrofitInstance.projectAPI.createProject(projectModel, getSusn(context),Utility.gettoken(context))

    }
}