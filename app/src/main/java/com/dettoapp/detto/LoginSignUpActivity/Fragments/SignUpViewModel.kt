package com.dettoapp.detto.LoginSignUpActivity.Fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.UtilityClasses.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel :ViewModel(){
    private val _validate=MutableLiveData<Resource<String>>()
    val validate:LiveData<Resource<String>>
    get() = _validate



    fun validate(email:String,password:String){
        viewModelScope.launch(Dispatchers.Default) {
            try{
                val regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$"
                if(email.matches(Regex(regex)))
                else _validate.postValue(Resource.Error(message = "not valid"))

                if((email.isEmpty()  || password.isEmpty()))
                    _validate.postValue(Resource.Error(message = "please fill deatils"))
                else
                    if(email.matches(Regex("[a-zA-Z]+[._A-Za-z0-9]*[@][a-zA-Z]+[.][a-zA-Z]+")))
                        _validate.postValue((Resource.Success("")))
                    else
                        _validate.postValue(Resource.Error(message = "please enter correct email"))

            }
            catch (e:Exception){

            }

        }
    }
}