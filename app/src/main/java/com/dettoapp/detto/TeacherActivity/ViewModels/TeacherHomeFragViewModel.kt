package com.dettoapp.detto.TeacherActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Chat.ChatServiceProvider
import com.dettoapp.detto.Models.AccessModel
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.TeacherModel
import com.dettoapp.detto.TeacherActivity.Repositories.TeacherRepository
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.UtilityClasses.Utility.toLowerAndTrim
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@SuppressLint("StaticFieldLeak")
class TeacherHomeFragViewModel(
    private val repository: TeacherRepository,
    private val context: Context
) : ViewModel() {

    val webServicesProvider = ChatServiceProvider()

    init {

        subscribeToSocketEvents()
        sendMessage()
    }

    private fun sendMessage() {
//        GlobalScope.launch(Dispatchers.IO) {
//            delay(3000L)
//            webServicesProvider.send("Hey From Android")
//        }
    }


    private fun subscribeToSocketEvents() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                webServicesProvider.startSocket("wss://detto.uk.to/chat/1234").buffer(10)
//                    .collect {
//                        Log.d("DDFF", "Collecting : ${it}")
////                    if (it.exception == null) {
////
////                    } else {
////                        //onSocketError(it.exception!!)
////                    }
//                    }
//            } catch (ex: Exception) {
//                Log.d("DDFF", "" + ex.localizedMessage)
//            }
//        }
    }

    private val _classRoomCreation = MutableLiveData<Resource<String>>()
    val classRoomCreation: LiveData<Resource<String>>
        get() = _classRoomCreation

    private val _classRoomDeletion = MutableLiveData<Resource<String>>()
    val classRoomDeletion: LiveData<Resource<String>>
        get() = _classRoomDeletion
    private val _access = MutableLiveData<Resource<String>>()
    val access: LiveData<Resource<String>>
        get() = _access
    private val _getTeacherModel = MutableLiveData<Resource<TeacherModel>>()
    val getTeacherModel: LiveData<Resource<TeacherModel>>
        get() = _getTeacherModel
    private val _accessChange = MutableLiveData<Resource<ArrayList<Classroom>>>()
    val accessChange: LiveData<Resource<ArrayList<Classroom>>>
        get() = _accessChange



    val allClassRooms = repository.getAllClassRooms()


    fun classRoomData(
        classroomName: String,
        sem: String,
        sec: String,
        teamSize: String,
        projectType: String,
        groupType: String
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _classRoomCreation.postValue(Resource.Loading())
                validate(classroomName, sem, sec, teamSize, projectType, groupType)
                val classroom = Classroom(

                    classroomName.toLowerAndTrim(),
                    sem.toLowerAndTrim(),
                    sec.toLowerAndTrim(),
                    Utility.createID(),
                    repository.getTeacherModel(),
                    repository.getClassroomSettingsModel(
                        teamSize.toLowerAndTrim(),
                        projectType.toLowerAndTrim(),
                        groupType.toLowerAndTrim()
                    )
                )
                repository.insertClassroom(classroom)
                repository.createClassroom(classroom)
                _classRoomCreation.postValue(Resource.Success(data = "Classroom created successfully"))


            } catch (e: Exception) {
                _classRoomCreation.postValue(Resource.Error(message = "Please enter all the fields" + e.localizedMessage))
            }
        }
    }


    fun deleteClassroom(classroom: Classroom) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _classRoomDeletion.postValue(Resource.Loading())
                repository.deleteClassroom(classroom)
                _classRoomDeletion.postValue(Resource.Success(data = ""))
            } catch (e: Exception) {
                _classRoomDeletion.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun addAccess(access: String, sem: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tid = Utility.getUID(context)
                val accessModel = AccessModel(access, sem)
                repository.addAccess(context, accessModel, tid)
                _access.postValue(Resource.Success("Success"))
            } catch (e: Exception) {
                _access.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun getTeacherModel(): TeacherModel {
        return repository.getTeacherModel()
    }
    fun getTeacherModelFromServer() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("PPW","jkjfkjd")
                val teacherModel =repository.getTeacherModelFromServer(Utility.getUID(context))
                _getTeacherModel.postValue(Resource.Success(teacherModel))
//                Log.d("PPW",t.toString())

            } catch (e: Exception) {
                _getTeacherModel.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun changeAccess(access: String, sem: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                    val classrooms = repository.changeAccess(access, sem)

                _accessChange.postValue(Resource.Success(classrooms))

            } catch (e: Exception) {
                _accessChange.postValue(Resource.Error(message = ""+e.localizedMessage))
            }

        }
    }

    fun getTeacherName() = repository.getTeacherName()

    private fun validate(
        classroomName: String,
        sec: String,
        sem: String,
        projectType: String,
        teamSize: String,
        groupType: String
    ) {
        if (classroomName.isEmpty() || sec.isEmpty() || sem.isEmpty() || projectType.isEmpty() || teamSize.isEmpty())
            throw Exception("Please Enter all Fields")
    }

}