package com.dettoapp.detto.LoginSignUpActivity.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.UtilityClasses.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginSignUpActivityViewModel : ViewModel() {


    private val _loginSignUp = MutableLiveData<Resource<String>>()
    val loginSignUp: LiveData<Resource<String>>
        get() = _loginSignUp



    fun loginProcess(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if(validate(email,password))
                {
                    _loginSignUp.postValue(Resource.Loading())
                    delay(1000)
                    _loginSignUp.postValue(Resource.Success(data = ""))
                }
                else
                {

                }
            } catch (e: Exception) {
                _loginSignUp.postValue(Resource.Error(message = ""+e.localizedMessage))
            }
        }
    }

    fun signUpProcess(name:String,usn:String,email: String, password: String,re_password:String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                signUpValidate(name,usn,email,password,re_password)
                _loginSignUp.postValue((Resource.Loading()))
                delay(1000)
                _loginSignUp.postValue((Resource.Success(data="registered")))
            } catch (e: Exception) {
                _loginSignUp.postValue(Resource.Error(message = ""+e.localizedMessage))
            }
        }
    }


    private fun validate(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty())
            throw Exception("enter all fields")
        else if (!email.matches(Regex("[a-zA-Z]+[._A-Za-z0-9]*[@][a-zA-Z]+[.][a-zA-Z]+")))
            throw Exception("Invalid Email")
        return true
    }
    private fun signUpValidate(name: String,usn:String,email: String, password: String,re_password:String) {
        if (email.isEmpty() || password.isEmpty()||usn.isEmpty()||re_password.isEmpty()||usn.isEmpty()||name.isEmpty())
            throw Exception("Enter all fields")
        else if (!email.matches(Regex("[a-zA-Z]+[._A-Za-z0-9]*[@][a-zA-Z]+[.][a-zA-Z]+")))
            throw Exception("Invalid Email")
        else if(!usn.matches(Regex("[1][Dd][Ss][1-9][0-9][A-Za-z][A-Za-z][0-9][0-9][0-9]")))
            throw Exception("Invalid USN")
        else if(password!=re_password)
            throw Exception("Passwords must match")
    }
}