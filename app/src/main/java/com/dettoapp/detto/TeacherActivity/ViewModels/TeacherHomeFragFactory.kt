package com.dettoapp.detto.TeacherActivity.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.TeacherActivity.TeacherRepository

class TeacherHomeFragFactory(
    private val repository: TeacherRepository,
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TeacherHomeFragViewModel(repository, context) as T
    }
}