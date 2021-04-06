package com.dettoapp.detto.StudentActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class StudentHomeFragViewModel(private val repository: StudentRepository, private val context: Context) : ViewModel() {
    private val _project1 = MutableLiveData<Resource<String>>()
    val project1: LiveData<Resource<String>>
        get() = _project1

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.getShouldFetch(context)) {
                _project1.postValue(Resource.Confirm(message = ""))
            }
        }
    }

    fun download() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _project1.postValue(Resource.Loading())
                repository.shouldFetch(context)
                _project1.postValue(Resource.Success(data = ""))

            } catch (e: Exception) {
                _project1.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    var allClassRooms = repository.getAllClassRooms()

}