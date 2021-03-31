package com.dettoapp.detto.StudentActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.StudentActivity.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class StudentClassDetailViewModel(private val repository: StudentRepository, private val context: Context) : ViewModel() {

    fun storeProject(title: String, description: String, usnMap: HashMap<Int, String>, classroom: Classroom) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.storeProjectInSharedPref(classroom.classroomuid, context)
            } catch (e: Exception) {

            }
        }
    }

    fun getProjectFromSharedPref(classroom: Classroom) = repository.getProjectFromSharedPref(classroom.classroomuid, context)
}