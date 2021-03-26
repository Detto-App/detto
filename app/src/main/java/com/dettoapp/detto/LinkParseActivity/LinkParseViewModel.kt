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
import java.lang.Exception

class LinkParseViewModel(private val repository: LinkParserRepository,private val context: Context):ViewModel() {
    private val _linkParse = MutableLiveData<Resource<String>>()
    val linkParse: LiveData<Resource<String>>
        get() = _linkParse

    fun validationOfUserWhoClickedTheLink(data:String){
        Log.d("DDDD","fjfldsjlkvmxv csacjsla")
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                _linkParse.postValue(Resource.Error(message = data))
                _linkParse.postValue(Resource.Loading())
                val role = repository.getRole(context)
                Log.d("DDDD","role $role")
                authenticate(role)
                Log.d("DDDD",data)

                val id:String=data.substring(data.length-36)
                Log.d("DDDD",""+id)

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
    private fun id(id:String){

    }
    private fun getType(data:String):String{
        if(data.contains("cid/"))
            return Constants.TYPE_CID
        return ""
    }
    private suspend fun getClassroom(id:String){
        val classroom = RetrofitInstance.createClassroomAPI.getC(id, Utility.gettoken(context))

        if(classroom.isSuccessful)
            Log.d("DDDD","dat "+Utility.gettoken(context))
        else
            Log.d("DDDD","dat"+classroom.errorBody()!!.string())

//            ?: throw Exception("Please Check Your User Role,Account Not Found \n"+id)

        //Log.d("DDDD","dat "+Utility.gettoken(context))
//        repository.insertClassroom(classroom)
    }
    private suspend fun compute(type:String,id:String){
        when(type){
            Constants.TYPE_CID ->getClassroom(id)

        }
    }
}