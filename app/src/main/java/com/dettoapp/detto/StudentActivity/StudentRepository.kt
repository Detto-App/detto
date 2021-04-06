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

class StudentRepository(private val dao: ClassroomDAO, private val projectDao: ProjectDAO) :
    BaseRepository() {
    fun getAllClassRooms() = dao.getAllClassRooms()

    fun getProjectFromSharedPref(classID: String, context: Context): Int {
        val sharedPreference =
            context.getSharedPreferences(Constants.PROJECT_CLASS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        return sharedPreference.getInt(classID, Constants.PROJECT_NOT_CREATED)
    }

    suspend fun insertProject(projectModel: ProjectModel) {
        projectDao.insertProject(projectModel)
    }

    private fun getSUsn() = Utility.STUDENT.susn.toLowerCase(Locale.ROOT)

    suspend fun insertProjectToServer(projectModel: ProjectModel) {
        RetrofitInstance.projectAPI.createProject(projectModel, getSUsn(), Utility.TOKEN)
    }

    suspend fun getProject(cid: String) = projectDao.getProject(cid)

    suspend fun checkProjectStatus(pid: String): ProjectModel {
        val projectModel =
            RetrofitInstance.projectAPI.getSingleProjectDetails(pid, Utility.TOKEN).body()
                ?: throw Exception("Unable to Find Classroom")
        updateProject(projectModel)
        return projectModel
    }
    suspend fun updateProject(projectModel: ProjectModel){
        projectDao.updateProject(projectModel)


    }
    suspend  fun storeEditedProject(cid:String,title:String,description:String):ProjectModel{
        val projectModelFromSharedPreferences=projectDao.getProject(cid)?:throw Exception("No class FOund")
        val projectModel=ProjectModel(projectModelFromSharedPreferences.pid,title,description,projectModelFromSharedPreferences.studentList,projectModelFromSharedPreferences.tid,projectModelFromSharedPreferences.cid,Constants.PROJECT_PENDING,projectModelFromSharedPreferences.studentNameList)!!
        updateProject(projectModel)
        RetrofitInstance.projectAPI.updateProject(projectModel,projectModel.pid,Utility.TOKEN)
        return projectModel

    }
}