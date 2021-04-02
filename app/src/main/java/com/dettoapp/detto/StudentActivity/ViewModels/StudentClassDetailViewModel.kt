package com.dettoapp.detto.StudentActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class StudentClassDetailViewModel(
        private val repository: StudentRepository,
        private val context: Context
) : ViewModel() {

    private val _stuProjectCreation = MutableLiveData<Resource<String>>()
    val stuProjectCreation: LiveData<Resource<String>>
        get() = _stuProjectCreation

    private val _project = MutableLiveData<Resource<ProjectModel>>()
    val project: LiveData<Resource<ProjectModel>>
        get() = _project


    fun getProject(cid: String) {
        viewModelScope.launch {
            try {
                val projectModel = repository.getProject(cid)
                if (projectModel == null)
                    _project.postValue(Resource.Error(message = "Not Found"))
                else
                    _project.postValue(Resource.Success(data = projectModel))
            } catch (e: Exception) {
                _project.postValue(Resource.Error(message = ""+e.localizedMessage))

            }
        }
    }

    fun storeProject(
            title: String,
            description: String,
            usnMap: HashMap<Int, String>,
            classroom: Classroom
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stuProjectCreation.postValue(Resource.Loading())
                validate(title, description, usnMap)
                val projectModel = ProjectModel(
                        Utility.createID(),
                        title,
                        description,
                        usnMap,
                        classroom.teacher.uid,
                        classroom.classroomuid
                )
                repository.insertProject(projectModel)
                _stuProjectCreation.postValue(Resource.Success(data = ""))
                //repository.storeProjectInSharedPref(classroom.classroomuid, context)
            } catch (e: Exception) {
                _stuProjectCreation.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    private fun validate(title: String, description: String, usnMap: HashMap<Int, String>) {

        if (title.isEmpty() || description.isEmpty() || usnMapIsEmpty(usnMap))
            throw Exception("not valid")

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


    fun getProjectFromSharedPref(classroom: Classroom) =
            repository.getProjectFromSharedPref(classroom.classroomuid, context)
}