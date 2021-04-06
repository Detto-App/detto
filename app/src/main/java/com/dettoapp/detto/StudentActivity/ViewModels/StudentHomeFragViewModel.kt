package com.dettoapp.detto.StudentActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Constants.SHOULD_FETCH
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class StudentHomeFragViewModel(private val repository: StudentRepository,  private val context: Context) : ViewModel() {
    private val _project1 = MutableLiveData<Resource<String>>()
    val project1: LiveData<Resource<String>>
        get() = _project1

    init {
        initialize()
    }
    private fun initialize(){
        viewModelScope.launch(Dispatchers.IO) {
            if(repository.getShouldFetch(context)) {
                _project1.postValue(Resource.Confirm(message = ""))
            }
        }
    }
    fun download(){
        viewModelScope.launch(Dispatchers.IO) {
            _project1.postValue(Resource.Loading())
            repository.shouldFetch(context)
            _project1.postValue(Resource.Success(data = ""))

        }
    }
    var allClassRooms = repository.getAllClassRooms()

}