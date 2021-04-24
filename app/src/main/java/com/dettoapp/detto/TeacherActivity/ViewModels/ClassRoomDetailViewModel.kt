package com.dettoapp.detto.TeacherActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.*
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import com.google.android.gms.tasks.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@SuppressLint("StaticFieldLeak")
class ClassRoomDetailViewModel(
        private val repository: ClassroomDetailRepository,
        private val context: Context
) : ViewModel() {

    private val TAG = "DDFF"

    private val _classroomStudents = MutableLiveData<Resource<List<StudentModel>>>()
    val classroomStudents: LiveData<Resource<List<StudentModel>>>
        get() = _classroomStudents

    private val _projectList = MutableLiveData<Resource<List<ProjectModel>>>()
    val projectList: LiveData<Resource<List<ProjectModel>>>
        get() = _projectList

    private val _deadline = MutableLiveData<Resource<List<DeadlineModel>>>()
    val deadline: LiveData<Resource<List<DeadlineModel>>>
        get() = _deadline

    fun getClassStudents(classroom: Classroom) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val classroomStudents = repository.getClassroomStudents(classroom.classroomuid)

                val customComparator = Comparator<StudentModel> { a, b ->
                    val aUsn: Int = a.susn.substring(a.susn.length - 3).toInt()
                    val bUsn: Int = b.susn.substring(a.susn.length - 3).toInt()
                    return@Comparator aUsn.compareTo(bUsn)
                }

                val list = ArrayList(classroomStudents.studentList)
                list.sortWith(customComparator)

                _classroomStudents.postValue(Resource.Success(data = list))
            } catch (e: Exception) {
                _classroomStudents.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun getProjects(cid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val project = repository.getProjects(cid)
                _projectList.postValue(Resource.Success(data = project))
            } catch (e: Exception) {
                _projectList.postValue(Resource.Error(message = "" + e.localizedMessage))
            }

        }
    }

    fun changeStatus(pid: String, status: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _projectList.postValue(Resource.Loading())
                repository.changeStatus(pid, status)
                _projectList.postValue((Resource.Confirm(message = "")))
            } catch (e: Exception) {
                _projectList.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun sendNotification(classroom: Classroom) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val x = Notification("Trial", "Trial Message")
                Firebase.messaging.unsubscribeFromTopic("/topics/${classroom.classroomuid}")
                val response = RetrofitInstance.notificationAPI.postNotification(PushNotification(x, "/topics/${classroom.classroomuid}"))
            } catch (e: Exception) {
                Log.d(TAG, e.toString())
            }
        }
    }

    fun getDeadline(classroomUid: String, dateRangePicker: MaterialDatePicker<Pair<Long, Long>>, reason :String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if(dateRangePicker.selection==null)
                    _deadline.postValue(Resource.Error(message = "Date not selected"))
                if(reason == "")
                    _deadline.postValue(Resource.Error(message = "reason field is empty"))

                val deadlineModel = DeadlineModel(Utility.createID(), reason, dateRangePicker.selection!!.first.toString(),
                                                    dateRangePicker.selection!!.second.toString())
                repository.createDeadline(deadlineModel, classroomUid)
            } catch (e: Exception) {
                _deadline.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun getDeadlineFromServer(cid: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val deadline = repository.getDeadline(cid)
//                val deadlineModel=DeadlineModel(";dsj","sfddxc","dscx","fdstyre")
//                val list=ArrayList<DeadlineModel>()
//                list.add(deadlineModel)
                _deadline.postValue(Resource.Success(data = deadline))
            } catch (e: Exception) {
                _deadline.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }
}
