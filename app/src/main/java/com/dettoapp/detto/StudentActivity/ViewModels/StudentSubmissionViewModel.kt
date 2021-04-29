package com.dettoapp.detto.StudentActivity.ViewModels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentSubmissionViewModel(private val repository: StudentRepository) :
    BaseViewModel() {
    init {
        getGDriveToken()
    }

    private fun getGDriveToken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("DDSS","GDRIVE TOKEN starting")
                val gDriveToken = repository.getGDriveToken()

                Log.d("DDSS", "GDRIVE TOKEN ${gDriveToken}")
            } catch (e: Exception) {
                Log.d("DDSS","GDRIVE TOKEN Error ${e.localizedMessage}")
            }
        }
    }
}