package com.dettoapp.detto.StudentActivity

import android.content.Context
import com.dettoapp.detto.Db.ClassroomDAO
import com.dettoapp.detto.Db.ProjectDAO
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.UtilityClasses.BaseRepository
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import java.util.*
import kotlin.collections.ArrayList

class StudentRepository(private val dao: ClassroomDAO, private val projectDao: ProjectDAO) : BaseRepository() {
    fun getAllClassRooms() = dao.getAllClassRooms()

//    fun getProjectFromSharedPref(classID: String, context: Context): Int {
//        val sharedPreference = context.getSharedPreferences(Constants.PROJECT_CLASS_FILE, Context.MODE_PRIVATE)
//                ?: throw Exception("Data Storage Exception")
//        return sharedPreference.getInt(classID, Constants.PROJECT_NOT_CREATED)
//    }

    suspend fun insertProject(projectModel: ProjectModel) {
        projectDao.insertProject(projectModel)
    }

    private fun getSUsn() = Utility.STUDENT.susn.toLowerCase(Locale.ROOT)

    suspend fun insertProjectToServer(projectModel: ProjectModel) {
        RetrofitInstance.projectAPI.createProject(projectModel, getSUsn(), Utility.TOKEN)
    }

    suspend fun getProject(cid: String) = projectDao.getProject(cid)

    suspend fun getManyProjectDetails():ArrayList<ProjectModel>{
        return RetrofitInstance.projectAPI.getManyProjectDetails(Utility.STUDENT.projects, Utility.TOKEN).body()!!
    }

    suspend fun shouldFetch(context:Context){
        val sharedPreference =
            context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")

            val listOfProjects=getManyProjectDetails()
            for(i in listOfProjects)
                insertProject(i)

        with(sharedPreference.edit())
        {
            putBoolean(Constants.SHOULD_FETCH,false)
            apply()
        }
    }

    suspend fun getShouldFetch(context:Context):Boolean{
        val sharedPreference =
            context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        return sharedPreference.getBoolean(Constants.SHOULD_FETCH, true)
    }
}