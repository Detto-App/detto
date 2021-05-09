package com.dettoapp.detto.StudentActivity.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.DeadlineModel
import com.dettoapp.detto.Models.Todo
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.BaseViewModel
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(
    private val repository: StudentRepository,
    private val context: Context
) : BaseViewModel() {

    private val _todo = MutableLiveData<Resource<List<Todo>>>()
    val todo: LiveData<Resource<List<Todo>>>
        get() = _todo

    fun createTodo(classroomuid: String, tittle:String, category:String, assigned:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val todo = Todo(
                    Utility.createID(), tittle,category,assigned)
                repository.createTodo(todo, classroomuid)
            } catch (e: Exception) {
                _todo.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun getTodo(cid: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val todo = repository.getTodo(cid)
                _todo.postValue(Resource.Success(data = todo))
            } catch (e: Exception) {
                _todo.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }
}