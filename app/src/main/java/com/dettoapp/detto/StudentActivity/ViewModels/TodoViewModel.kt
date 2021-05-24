package com.dettoapp.detto.StudentActivity.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.DeadlineModel
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.Models.Todo
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.BaseViewModel
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TodoViewModel(
    private val repository: StudentRepository,
    private val context: Context
) : BaseViewModel() {

    private val _todo = MutableLiveData<Resource<List<Todo>>>()
    val todo: LiveData<Resource<List<Todo>>>
        get() = _todo

    private lateinit var  pid:String

    private val _project1 = MutableLiveData<Resource<List<String>>>()
    val project1: LiveData<Resource<List<String>>>
        get() = _project1


    fun createTodo( tittle:String, category:String, assigned:String,status:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val todo = Todo(
                    Utility.createID(), tittle,category,assigned,status)
                repository.createTodo(todo, pid)
            } catch (e: Exception) {
                _todo.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun getTodo(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //Keep it, It is to ease the UI loading as many items may load at first
                delay(200)
                val todo = repository.getTodo(pid)
                _todo.postValue(Resource.Success(data = todo))
            } catch (e: Exception) {
                _todo.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun getProjectFromSharedPrefForTodo(cid: String) {
        pid = repository.getProjectFromSharedPrefForTodo(cid, context)
    }

    fun getRoles() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("cvb","vikas")
                val projectModel = repository.getRoles(pid)
                _project1.postValue(Resource.Success(data = projectModel!!))
                Log.d("cvb",projectModel.toString())
            }catch (e:Exception){
                _project1.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun deleteTodo(toid:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteTodo(pid, toid)
            } catch (e: Exception) {
                _todo.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun changeStatus(toid:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
               repository.changeStatus(toid,pid)
            } catch (e: Exception) {
                _todo.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }
}