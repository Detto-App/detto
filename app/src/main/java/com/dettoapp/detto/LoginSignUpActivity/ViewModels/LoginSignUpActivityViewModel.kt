package com.dettoapp.detto.LoginSignUpActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.LoginSignUpActivity.LoginSignUpRepository
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.Models.TeacherModel
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@SuppressLint("StaticFieldLeak")
class LoginSignUpActivityViewModel(private val repository: LoginSignUpRepository,  private val context: Context) : ViewModel() {


    private val _loginSignUp = MutableLiveData<Resource<String>>()
    val loginSignUp: LiveData<Resource<String>>
        get() = _loginSignUp


    fun loginProcess(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loginSignUp.postValue(Resource.Loading())
                if(validate(email,password))
                {
                    Firebase.auth.signInWithEmailAndPassword(email, password).await()
                    repository.setLoginData(context,email)
                    _loginSignUp.postValue(Resource.Success(data = "Login Successful"))
                }
            } catch (e: Exception) {
                Log.d("poiu",e.localizedMessage)
                _loginSignUp.postValue(Resource.Error(message = ""+e.localizedMessage))
            }
        }
    }

    fun signUpProcess(role:Int,name:String,usn:String,email: String, password: String,re_password:String) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                _loginSignUp.postValue((Resource.Loading()))
                Firebase.auth.createUserWithEmailAndPassword(email,password).await()
                signUpValidate(role,name,usn,email,password,re_password)
                val uid = Utility.createID()
                if(role==0){
                    val teacherModel= TeacherModel(name,email,uid)
                    repository.sendTeacherData(teacherModel)
                }else {
                    val studentModel= StudentModel(name,email,uid,usn)
                    repository.sendStudentData(studentModel)
                }
                repository.setSignUpData(context,email,role,name,usn,uid)
                _loginSignUp.postValue((Resource.Success(data="Registered")))
            } catch (e: Exception) {
                _loginSignUp.postValue(Resource.Error(message = ""+e.localizedMessage))
            }
        }
    }

    private fun validate(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty())
            throw Exception(Constants.ERROR_FILL_ALL_FIELDS)
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