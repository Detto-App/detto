package com.dettoapp.detto.LinkParseActivity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
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
                    //user not logged in
                } else if (role == Constants.TEACHER) {
                    throw Exception("Unable to because the user is teacher")
//                        teacher not allowed
                } else if (role == Constants.STUDENT) {

                }

            }catch (e:Exception){
                _linkParse.postValue(Resource.Error(message = e.localizedMessage))
            }
        }
    }
}