package com.dettoapp.detto.StudentActivity.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.TeacherActivity.TeacherRepository

class StudentHomeFragFactory(private val repository: StudentRepository, private val context: Context): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StudentHomeFragViewModel(repository,context) as T
    }
}