package com.dettoapp.detto.LinkParseActivity

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
//import com.dettoapp.detto.TeacherActivity.db.Classroom
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.Exception

class LinkParseViewModel(private val repository: LinkParserRepository,private val context: Context):ViewModel() {
    private lateinit var tempClassroom: Classroom
    private val _linkParse = MutableLiveData<Resource<String>>()
    val linkParse: LiveData<Resource<String>>
        get() = _linkParse

    fun validationOfUserWhoClickedTheLink(data:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _linkParse.postValue(Resource.Loading())

                val role = repository.getRole(context)
                authenticate(role)

                val id:String= getID(data)
                val type=getType(data)

                compute(type,id)

            }catch (e:Exception){
                _linkParse.postValue(Resource.Error(message = e.localizedMessage))
            }
        }
    }
    private fun authenticate(role :Int){
        if (role == -1) {
            throw Exception("User not logged in!!. Try again after Logging in")
        } else if (role == Constants.TEACHER) {
            throw Exception("Unable to because the user is teacher")
        }
    }
    private fun getID(data:String) = data.substring(data.length-36)


    private fun getType(data:String):String{
        if(data.contains("cid/"))
            return Constants.TYPE_CID
        return ""
    }

    private suspend fun getClassroom(id:String){
        val classroom = RetrofitInstance.createClassroomAPI.getClassroom(id, Utility.gettoken(context)).body()?:
            throw Exception("Unable to Find Classroom")
        val classRoomDetails="Classroom Name: "+classroom.classroomname+"\nCreated by: "+classroom.teacher.name+"\nSection: "+classroom.section+"\nSem: "+classroom.sem
       tempClassroom=classroom
        _linkParse.postValue(Resource.Confirm(message = classRoomDetails))
    }


     fun insertClassroom(){
         viewModelScope.launch(Dispatchers.IO) {
             try {
                 repository.insertClassroom(tempClassroom)
                 val studentModel=Utility.getStudentModel(context)
                 repository.regStudentToClassroom(context,studentModel,tempClassroom.classroomuid)
                 _linkParse.postValue(Resource.Success(""))
             }
             catch (e:Exception){
                 _linkParse.postValue(Resource.Error(message = "You have Already Joined The Classroom: "+tempClassroom.classroomname))
             }
         }
    }

    private suspend fun compute(type:String,id:String){
        when(type){
            Constants.TYPE_CID ->getClassroom(id)
        }
    }
}