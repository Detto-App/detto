package com.dettoapp.detto.LoginSignUpActivity.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.UtilityClasses.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



class LoginSignUpActivityViewModel : ViewModel() {


    private val _loginSignUp = MutableLiveData<Resource<String>>()
    val loginSignUp: LiveData<Resource<String>>
        get() = _loginSignUp

    lateinit var auth: FirebaseAuth


    fun loginProcess(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                _loginSignUp.postValue(Resource.Loading())
                if( validate(email,password))
                {

                    Firebase.auth.signInWithEmailAndPassword(email, password).await()
                    _loginSignUp.postValue(Resource.Success(data = "Login Sucessfull"))
                }
            } catch (e: Exception) {

                _loginSignUp.postValue(Resource.Error(message = ""+e.localizedMessage))
            }
        }
    }

    fun signUpProcess(role:Int,name:String,usn:String,email: String, password: String,re_password:String) {
        viewModelScope.launch(Dispatchers.IO) {
            lateinit var auth: FirebaseAuth
            auth = Firebase.auth

            try {
                signUpValidate(role,name,usn,email,password,re_password)
                _loginSignUp.postValue((Resource.Loading()))
                auth.createUserWithEmailAndPassword(email,password).await()
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
    private fun signUpValidate(role:Int,name: String,usn:String,email: String, password: String,re_password:String) {
        val validation= email.isEmpty() || password.isEmpty()||re_password.isEmpty()||name.isEmpty()
        if ((role==0 && validation ) || (role==1 &&(validation || usn.isEmpty())))
            throw Exception("Enter all fields")
        else if (!email.matches(Regex("[a-zA-Z]+[._A-Za-z0-9]*[@][a-zA-Z]+[.][a-zA-Z]+")))
            throw Exception("Invalid Email")
        else if(role==1 && !usn.matches(Regex("[1][Dd][Ss][1-9][0-9][A-Za-z][A-Za-z][0-9][0-9][0-9]")))
            throw Exception("Invalid USN")
        else if(password!=re_password)
            throw Exception("Passwords must match")
    }
}