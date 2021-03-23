package com.dettoapp.detto.TeacherActivity.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.TeacherActivity.db.ClassroomRepository
import java.lang.IllegalArgumentException

class ClassroomViewModelFactory(private val repository: ClassroomRepository):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ClassroomViewModel::class.java)){
            return ClassroomViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }

}