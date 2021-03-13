package com.dettoapp.detto.LoginSignUpActivity.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.UtilityClasses.Resource
import kotlinx.coroutines.Dispatchers
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

                }
                else
                {

                }
            } catch (e: Exception) {
                _loginSignUp.postValue(Resource.Error(message = ""+e.localizedMessage))
            }
        }
    }

    fun signUpProcess(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if(validate(email,password))
                {

                }
                else
                {

                }
            } catch (e: Exception) {
                _loginSignUp.postValue(Resource.Error(message = ""+e.localizedMessage))
            }
        }
    }


    private fun validate(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty())
            return false
        else if (email.matches(Regex("[a-zA-Z]+[._A-Za-z0-9]*[@][a-zA-Z]+[.][a-zA-Z]+")))
            return false

        return true
    }
}