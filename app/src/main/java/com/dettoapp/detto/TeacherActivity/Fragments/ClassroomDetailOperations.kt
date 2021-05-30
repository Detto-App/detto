package com.dettoapp.detto.TeacherActivity.Fragments

import androidx.lifecycle.ViewModelStoreOwner
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel

interface ClassroomDetailOperations {
    fun getClassroomStudents()

    fun getProjects()

    fun getViewModel(): ClassRoomDetailViewModel

    fun getViewModelStoreOwner(): ViewModelStoreOwner

    fun getClassroom(): String

    fun getDeadlineFromServer()
}