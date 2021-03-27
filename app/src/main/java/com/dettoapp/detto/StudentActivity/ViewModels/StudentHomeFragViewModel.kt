package com.dettoapp.detto.StudentActivity.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dettoapp.detto.StudentActivity.StudentRepository

class StudentHomeFragViewModel(private val repository: StudentRepository, private val context: Context) : ViewModel() {


    var allClassRooms = repository.getAllClassRooms()

}