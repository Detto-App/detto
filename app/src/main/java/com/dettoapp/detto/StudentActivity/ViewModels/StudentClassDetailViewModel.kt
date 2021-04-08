package com.dettoapp.detto.StudentActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.BaseViewModel
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.UtilityClasses.Utility.toHashSet
import com.dettoapp.detto.UtilityClasses.Utility.toLowerAndTrim
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class StudentClassDetailViewModel(
        private val repository: StudentRepository,
        private val context: Context
) : BaseViewModel() {

    private val _stuProjectCreation = MutableLiveData<Resource<ProjectModel>>()
    val stuProjectCreation: LiveData<Resource<ProjectModel>>
        get() = _stuProjectCreation

    private val _project = MutableLiveData<Resource<ProjectModel>>()
    val project: LiveData<Resource<ProjectModel>>
        get() = _project


    fun getProject(cid: String) {
        operateWithLiveData(_project, mainFunction = {
            val projectModel = repository.getProject(cid)
            if (projectModel == null)
                it.postValue(Resource.Error(message = "Not Found"))
            else
                it.postValue(Resource.Success(data = projectModel))
        })
    }

    fun storeProject(
            title: String,
            description: String,
            usnMap: HashMap<Int, String>,
            classroom: Classroom,
            arrayList: ArrayList<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val usnMapSet = usnMap.toHashSet()
                _stuProjectCreation.postValue(Resource.Loading())
                validate(title, description, usnMap, arrayList, usnMapSet)
                val projectModel = ProjectModel(
                        Utility.createID(),
                        title.toLowerAndTrim(),
                        description.toLowerAndTrim(),
                        usnMapSet,
                        classroom.teacher.uid,
                        classroom.classroomuid,
                        studentNameList = arrayListOf(Utility.STUDENT.name)
                )
                repository.insertProject(projectModel)
                repository.insertProjectToServer(projectModel)
                _stuProjectCreation.postValue(Resource.Success(data = projectModel))
            } catch (e: Exception) {
                _stuProjectCreation.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    private fun validate(title: String, description: String, usnMap: HashMap<Int, String>, arrayList: ArrayList<String>, usnMapSet: Set<String>) {
        if (title.trim().isEmpty() || usnMapIsEmpty(usnMap))
            throw Exception("Title not Present")
        else if (description.trim().isEmpty())
            throw Exception("Description Not present")
        else if (usnMapSet.size != arrayList.size)
            throw Exception("Duplicate USNs found")
        else if (usnMap.size < 2 || usnMapSet.size < 2)
            throw Exception("Minimum 2 USNs Required")

    }

    private fun usnMapIsEmpty(usnMap: HashMap<Int, String>): Boolean {
        for ((k, v) in usnMap) {
            if (v.isEmpty())
                throw Exception("Empty usn")
            if (!v.matches(Regex("[1][Dd][Ss][1-9][0-9][A-Za-z][A-Za-z][0-9][0-9][0-9]")))
                throw Exception("Invalid USN Syntax")
        }
        return false
    }

    fun checkProjectStatus(pid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val projectModel = repository.checkProjectStatus(pid)
                _project.postValue(Resource.Success(projectModel))


            } catch (e: Exception) {
                _project.postValue(Resource.Error(message = "" + e.localizedMessage))
            }

        }
    }

    fun storeEditedProject(cid: String, title: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val projectModel = repository.storeEditedProject(cid, title, description)
                _project.postValue(Resource.Success(projectModel))
            } catch (e: Exception) {
                _project.postValue(Resource.Error(message = "" + e.localizedMessage))
            }

        }

    }


    fun getProjectFromSharedPref(classroom: Classroom) =
            repository.getProjectFromSharedPref(classroom.classroomuid, context)

}

