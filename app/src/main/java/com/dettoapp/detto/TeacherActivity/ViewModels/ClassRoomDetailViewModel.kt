package com.dettoapp.detto.TeacherActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

@SuppressLint("StaticFieldLeak")
class ClassRoomDetailViewModel(
    private val repository: ClassroomDetailRepository,
    private val context: Context
) : ViewModel() {


    private val _classroomStudents = MutableLiveData<Resource<List<StudentModel>>>()
    val classroomStudents: LiveData<Resource<List<StudentModel>>>
        get() = _classroomStudents

    fun getClassStudents(classroom: Classroom) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val classroomStudents = repository.getClassroomStudents(
                    classroom.classroomuid,
                    Utility.gettoken(context)
                )
                _classroomStudents.postValue(Resource.Success(data = ArrayList(classroomStudents.studentList)))
            } catch (e: Exception) {
                _classroomStudents.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }
}