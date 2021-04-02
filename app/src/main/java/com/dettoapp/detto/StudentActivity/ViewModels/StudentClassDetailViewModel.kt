package com.dettoapp.detto.StudentActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.Models.TeacherModel
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class StudentClassDetailViewModel(private val repository: StudentRepository, private val context: Context) : ViewModel() {

    private val _stuViewModel = MutableLiveData<Resource<Int>>()
    val stuViewModel: LiveData<Resource<Int>>
        get() = _stuViewModel

    fun storeProject(title: String, description: String, usnMap: HashMap<Int, String>, classroom: Classroom) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("asdf",usnMap.toString())
                validate(title,description,usnMap)
                val projectModel= ProjectModel(Utility.createID(),title,description,usnMap,classroom.teacher.uid,classroom.classroomuid)
                repository.insertProject(projectModel)
                repository.insertProjectToServer(projectModel,context)
                //repository.storeProjectInSharedPref(classroom.classroomuid, context)
            } catch (e: Exception) {
                _stuViewModel.postValue(Resource.Error(message = ""+e.localizedMessage))
            }
        }
    }
    private fun validate(title: String, description: String, usnMap: HashMap<Int, String>){

            if (title.isEmpty() || description.isEmpty() || usnmapIsEmpty(usnMap))
                throw Exception("not valid")

    }

    private fun usnmapIsEmpty(usnMap: HashMap<Int, String>): Boolean {
        for((k,v) in usnMap){
            if(v.isEmpty())
                throw Exception("Empty usn")
            if(!v.matches(Regex("[1][Dd][Ss][1-9][0-9][A-Za-z][A-Za-z][0-9][0-9][0-9]")))
                throw Exception("Invalid USN Syntax")
        }
        return false
    }


    fun getProjectFromSharedPref(classroom: Classroom) = repository.getProjectFromSharedPref(classroom.classroomuid, context)
}