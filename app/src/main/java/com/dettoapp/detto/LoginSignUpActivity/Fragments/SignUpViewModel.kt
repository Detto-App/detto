package com.dettoapp.detto.LoginSignUpActivity.Fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.UtilityClasses.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class SignUpViewModel :ViewModel(){
    private val _validate=MutableLiveData<Resource<String>>()
    val validate:LiveData<Resource<String>>
    get() = _validate



    fun validate(email:String,password:String){
        viewModelScope.launch(Dispatchers.Default) {
            try{
                if((email.isEmpty()  || password.isEmpty()))
                    _validate.postValue(Resource.Error(message = "please fill deatils"))
                else
                    _validate.postValue(Resource.Success(""))

            }
            catch (e:Exception){

            }

        }
    }
}