package com.dettoapp.detto.TeacherActivity.Fragments

import androidx.lifecycle.ViewModelStoreOwner
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel

interface ClassroomDetailOperations
{
    fun getClassroomStudents()

    fun getProjects()

    fun getViewModel():ClassRoomDetailViewModel

    fun getViewModelStoreOwner():ViewModelStoreOwner
}