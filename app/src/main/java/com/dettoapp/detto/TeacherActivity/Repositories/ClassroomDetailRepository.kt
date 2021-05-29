package com.dettoapp.detto.TeacherActivity.Repositories

import android.util.Log
import com.dettoapp.detto.Db.RubricsDAO
import com.dettoapp.detto.Models.*
import com.dettoapp.detto.UtilityClasses.BaseRepository
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import okhttp3.ResponseBody

class ClassroomDetailRepository(private val dao: RubricsDAO) : BaseRepository() {

    suspend fun getClassroomStudents(classID: String): ClassRoomStudents {
        return RetrofitInstance.createClassroomAPI.getClassroomStudents(classID, Utility.TOKEN).body()
            ?: throw Exception("Unable to Fetch Exception")
    }

    suspend fun getProjects(classID: String): List<ProjectModel> {
        return RetrofitInstance.projectAPI.getProjects(classID, Utility.TOKEN).body()
            ?: throw Exception("Unable to Fetch Project")
    }

    suspend fun changeStatus(pid: String, status: String): ResponseBody {
        return RetrofitInstance.projectAPI.changeStatus(pid, status, Utility.TOKEN).body()
            ?: throw Exception("Unable to Change Status")
    }

    suspend fun createDeadline(deadlineModel: DeadlineModel, cid: String) {
        RetrofitInstance.projectAPI.createDeadline(deadlineModel, Utility.TOKEN, cid)
    }

    suspend fun getDeadline(classID: String): List<DeadlineModel> {
        return RetrofitInstance.projectAPI.getDeadline(classID, Utility.TOKEN).body()
            ?: throw Exception("Unable to Fetch Deadline")
    }

    suspend fun insertRubricsToServer(rubricsModel: RubricsModel) {
        RetrofitInstance.createClassroomAPI.createRubrics(rubricsModel, Utility.TOKEN)
        dao.insertRubrics(rubricsModel)

    }

    suspend fun getRubricsFromDAO(cid: String): RubricsModel {
        val rubricsModel = dao.getRubrics(cid)
        if (rubricsModel == null)
            return getRubrics(cid)
        else
            return rubricsModel
    }

    suspend fun getRubricsForProject(cid: String, pid: String): ArrayList<ProjectRubricsModel>? {
        val projectRubricsList =
            RetrofitInstance.createClassroomAPI.getRubricsForProject(cid, pid, Utility.TOKEN).body()
//    Log.d("WEE",projectRubricsList.toString())
        return projectRubricsList
    }

    suspend fun getRubrics(classID: String): RubricsModel {

        val rubricsModel = dao.getRubrics(classID)
        if (rubricsModel == null) {
            RetrofitInstance.createClassroomAPI.getRubrics(classID, Utility.TOKEN).body()
                ?: throw Exception("Unable to Fetch Rubrics")
            dao.insertRubrics(rubricsModel)
        }
        return rubricsModel
    }

    suspend fun insertProjectRubricsToServer(projectRubricsList: ArrayList<ProjectRubricsModel>) {
        RetrofitInstance.createClassroomAPI.insertProjectRubrics(projectRubricsList, Utility.TOKEN)
    }

    suspend fun updateProjectRubrics(studentRubricsMap: HashMap<String, RubricsModel>, cid: String, pid: String) {

        RetrofitInstance.createClassroomAPI.updateProjectRubrics(studentRubricsMap, cid, pid, Utility.TOKEN)

    }

    suspend fun formTeams(projectModelList: ArrayList<ProjectModel>, cid: String) {
        Log.d("WRR", projectModelList.toString())
        RetrofitInstance.projectAPI.createProjects(projectModelList, cid, Utility.TOKEN)

    }


}