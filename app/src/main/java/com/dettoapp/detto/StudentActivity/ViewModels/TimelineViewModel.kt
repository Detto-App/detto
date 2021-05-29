package com.dettoapp.detto.StudentActivity.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.DeadlineModel
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.Models.Timeline
import com.dettoapp.detto.Models.Todo
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.BaseViewModel
import com.dettoapp.detto.UtilityClasses.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimelineViewModel(
    private val repository: StudentRepository,
    private val context: Context
) : BaseViewModel() {

    private lateinit var pid: String

    private val _timeline = MutableLiveData<Resource<List<Timeline>>>()
    val timeline: LiveData<Resource<List<Timeline>>>
        get() = _timeline

    fun getProjectFromSharedPrefForTodo(cid: String) {
        pid = repository.getProjectFromSharedPrefForTodo(cid, context)
    }

    fun getTimeline() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("123", "3")
                //Keep it, It is to ease the UI loading as many items may load at first
                delay(200)
                val list = repository.getTimeline(pid)
                val customComparator = Comparator<Timeline> { a, b ->
                    val aDate: Long = a.date.toLong()
                    val bDate: Long = b.date.toLong()
                    return@Comparator aDate.compareTo(bDate)
                }

                val reslist = ArrayList(list)
                reslist.sortWith(customComparator)

                _timeline.postValue(Resource.Success(data = list))
            } catch (e: Exception) {
                _timeline.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }
}