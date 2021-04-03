package com.dettoapp.detto.LinkParseActivity

import android.content.Context
import com.dettoapp.detto.Db.ClassroomDAO
import com.dettoapp.detto.Db.ProjectDAO
//import com.dettoapp.detto.Db.StudentClassroomDAO
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import java.util.*

class LinkParserRepository(private val dao: ClassroomDAO,private val projectDAO: ProjectDAO) {
    fun getRole(context: Context): Int {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        return sharedPreference.getInt(Constants.USER_ROLE_KEY, -1)
    }

    suspend fun insertClassroom(classroom: Classroom) {
        dao.insertClassroom(classroom)
    }
    suspend fun insertProject(projectModel:ProjectModel){
        projectDAO.insertProject(projectModel)
    }

    suspend fun regStudentToClassroom(studentModel: StudentModel, cid: String) {
        RetrofitInstance.createClassroomAPI.regStudentToClassroom(studentModel, cid, Utility.TOKEN)
    }
    suspend fun regStudentToProject(pid:String){
        RetrofitInstance.projectAPI.regStudentToProject(pid,getSname(),Utility.TOKEN)
    }
     fun getSusn()=Utility.STUDENT.susn.toLowerCase(Locale.ROOT)
     fun getSname()=Utility.STUDENT.name.toLowerCase(Locale.ROOT)
    suspend fun getProject(pid: String) = RetrofitInstance.projectAPI.getProject(pid,Utility.TOKEN)
}