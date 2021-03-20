package com.dettoapp.detto.StudentActivity.ViewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.Resource

class StudentHomeFragViewModel(private val repository: StudentRepository, private val context: Context) : ViewModel() {
    private val _student = MutableLiveData<Resource<String>>()
    val student: LiveData<Resource<String>>
        get() = _student
}