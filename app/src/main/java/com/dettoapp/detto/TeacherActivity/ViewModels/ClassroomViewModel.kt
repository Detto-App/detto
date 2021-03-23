package com.dettoapp.detto.TeacherActivity.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.TeacherActivity.db.Classroom
import com.dettoapp.detto.TeacherActivity.db.ClassroomRepository
import kotlinx.coroutines.launch

class ClassroomViewModel(private val repository: ClassroomRepository):ViewModel() {
    fun insert(classroom: Classroom){
        viewModelScope.launch {
            repository.insert(classroom )
        }
    }
}