package com.dettoapp.detto.LinkParseActivity

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.TeacherActivity.db.Classroom
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _linkParse.postValue(Resource.Error(message = data))
                _linkParse.postValue(Resource.Loading())
                val role = repository.getRole(context)
                if (role == -1) {
                    throw Exception("User not logged in!!. Try again after Logging in")
                } else if (role == Constants.TEACHER) {
                    throw Exception("Unable to because the user is teacher")
                } else if (role == Constants.STUDENT) {
                    Log.d("poi",data)
                    val id:String=data.substring(data.length-32)
                    Log.d("poi",""+id)
                    val classroom: Classroom = RetrofitInstance.createClassroomAPI.getClassroom(id, Utility.gettoken(context)).body()
                        ?: throw Exception("invalid link")

                }

            }catch (e:Exception){
                _linkParse.postValue(Resource.Error(message = e.localizedMessage))
            }
        }
    }
}