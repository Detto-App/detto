package com.dettoapp.detto.StudentActivity

import android.content.Context
import android.util.Log
import com.dettoapp.detto.Db.ClassroomDAO
import com.dettoapp.detto.Db.ProjectDAO
import com.dettoapp.detto.Models.DeadlineModel
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.Models.Timeline
import com.dettoapp.detto.Models.Todo
import com.dettoapp.detto.UtilityClasses.BaseRepository
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import okhttp3.ResponseBody
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

    private suspend fun insertProject(listOfProjectModel: List<ProjectModel>) {
        projectDao.insertProject(listOfProjectModel)
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


    suspend fun updateProject(projectModel: ProjectModel) {
        projectDao.updateProject(projectModel)
    }


    suspend fun storeEditedProject(cid: String, title: String, description: String): ProjectModel {
        val projectModelFromDatabase =
            projectDao.getProject(cid) ?: throw Exception("No class FOund")
        val projectModel = ProjectModel(
            projectModelFromDatabase.pid,
            title,
            description,
            projectModelFromDatabase.studentList,
            projectModelFromDatabase.tid,
            projectModelFromDatabase.cid,
            Constants.PROJECT_PENDING,
            projectModelFromDatabase.studentNameList
        )
        updateProject(projectModel)
        RetrofitInstance.projectAPI.updateProject(projectModel, projectModel.pid, Utility.TOKEN)
        return projectModel
    }

    private suspend fun getManyProjectDetails(): ArrayList<ProjectModel> {
        return RetrofitInstance.projectAPI.getManyProjectDetails(
            Utility.STUDENT.projects,
            Utility.TOKEN
        ).body()
            ?: throw Exception("Unable to Fetch Projects")
    }

    suspend fun shouldFetch(context: Context) {
        val sharedPreference =
            context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")

        val listOfProjects = getManyProjectDetails()

        insertProject(listOfProjects)

        with(sharedPreference.edit())
        {
            putBoolean(Constants.SHOULD_FETCH, false)
            apply()
        }
    }

    fun getShouldFetch(context: Context): Boolean {
        val sharedPreference =
            context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        return sharedPreference.getBoolean(Constants.SHOULD_FETCH, true)
    }

    suspend fun getClassRoomList() = dao.getAllClassRoomList()

    suspend fun getDeadline(classID: String): List<DeadlineModel> {
        return RetrofitInstance.projectAPI.getDeadline(classID, Utility.TOKEN).body()
            ?: throw Exception("Unable to Fetch Deadline")
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getGDriveToken() = RetrofitInstance.gDriveAPI.getGDriveToken().body()?.string()
        ?: throw Exception("Unable to Fetch GDrive token")

    suspend fun createTodo(todo: Todo, pid: String) {
        RetrofitInstance.projectAPI.createTodo(todo, Utility.TOKEN, pid)
    }

    suspend fun getTodo(pid: String): List<Todo> {
        return RetrofitInstance.projectAPI.getTodo(pid, Utility.TOKEN).body()
            ?: throw Exception("Unable to Fetch Todo")
    }

    suspend fun getTimeline(pid: String): List<Timeline> {
        Log.d("123", "4")
        return RetrofitInstance.projectAPI.getTimeline(pid, Utility.TOKEN).body()
            ?: throw Exception("Unable to Fetch Timeline" + pid)
    }

    fun storeProjectIdinSharedPref(cid: String, pid: String, context: Context) {
        val sharedPreference =
            context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")

        with(sharedPreference.edit())
        {
            putString(cid, pid)
            apply()
        }
    }

    fun getProjectFromSharedPrefForTodo(cid: String, context: Context): String {
        val sharedPreference =
            context.getSharedPreferences(Constants.CLASS_PROJECT, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")

        return sharedPreference.getString(cid, "")!!
    }

    suspend fun getRoles(pid: String): List<String> {
        Log.d("cvb", "vi")
        return projectDao.getProjectUsingPid(pid)!!.projectStudentList.values.toList()
    }

    suspend fun deleteTodo(pid: String, toid: String): ResponseBody {
        return RetrofitInstance.projectAPI.deleteTodo(pid, toid, Utility.TOKEN).body()
            ?: throw Exception("Unable to Delete Todo")
    }

    suspend fun changeStatus(toid: String, pid: String): ResponseBody {
        return RetrofitInstance.projectAPI.changeStatusOfTodo(toid, pid, Utility.TOKEN).body()
            ?: throw Exception("Data Storage Exception")
    }

    suspend fun getStudentNameList(usnMapSet: HashSet<String>): HashMap<String, String> {
        return RetrofitInstance.projectAPI.getStudentNameList(usnMapSet, Utility.TOKEN).body()
            ?: throw  Exception("Illegal USN")
    }


}