package com.dettoapp.detto.StudentActivity

import androidx.lifecycle.ViewModelStoreOwner
import com.dettoapp.detto.Models.ProjectModel

interface StudentOperations {
    fun getViewModelOwner():ViewModelStoreOwner
    fun getProjectModel() :ProjectModel
}