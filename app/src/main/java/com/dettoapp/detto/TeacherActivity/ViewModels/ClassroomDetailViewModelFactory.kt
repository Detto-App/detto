package com.dettoapp.detto.TeacherActivity.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository

class ClassroomDetailViewModelFactory(
    private val repository: ClassroomDetailRepository,
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ClassRoomDetailViewModel(repository, context) as T
    }
}