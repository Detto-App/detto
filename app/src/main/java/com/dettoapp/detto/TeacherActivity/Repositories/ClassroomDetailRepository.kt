package com.dettoapp.detto.TeacherActivity.Repositories

import com.dettoapp.detto.Models.ClassRoomStudents
import com.dettoapp.detto.Models.DeadlineModel
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.UtilityClasses.BaseRepository
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import okhttp3.ResponseBody

class ClassroomDetailRepository:BaseRepository() {

    suspend fun getClassroomStudents(classID: String): ClassRoomStudents {
        return RetrofitInstance.createClassroomAPI.getClassroomStudents(classID, Utility.TOKEN).body()
                ?: throw Exception("Unable to Fetch Exception")
    }

    suspend fun getProjects(classID: String): List<ProjectModel> {
        return RetrofitInstance.projectAPI.getProjects(classID,Utility.TOKEN).body()
            ?: throw Exception("Unable to Fetch Project")
    }

    suspend fun changeStatus(pid: String,status:String): ResponseBody    {
        return RetrofitInstance.projectAPI.changeStatus(pid,status,Utility.TOKEN).body()
            ?: throw Exception("Unable to Change Status")
    }

    suspend fun createDeadline(deadlineModel: DeadlineModel, cid:String){
        RetrofitInstance.projectAPI.createDeadline(deadlineModel, Utility.TOKEN,cid)
    }

    suspend fun getDeadline(classID: String): List<DeadlineModel>{
        return RetrofitInstance.projectAPI.getDeadline(classID,Utility.TOKEN).body()
            ?: throw Exception("Unable to Fetch Deadline")
    }

}