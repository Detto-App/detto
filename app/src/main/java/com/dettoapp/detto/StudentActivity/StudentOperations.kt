package com.dettoapp.detto.StudentActivity

import androidx.lifecycle.ViewModelStoreOwner

interface StudentOperations {
    fun getViewModelOwner(): ViewModelStoreOwner
    fun getTodo()
}