package com.dettoapp.detto.loginActivity.ViewModels

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
class LoginSignUpActivityViewModel(
    private val repository: LoginSignUpRepository,
    private val context: Context
) : ViewModel() {


    private val _login = MutableLiveData<Resource<Int>>()
    val login: LiveData<Resource<Int>>
        get() = _login

    private val _signup = MutableLiveData<Resource<Int>>()
    val signup: LiveData<Resource<Int>>
        get() = _signup


    fun loginProcess(role: Int, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _login.postValue(Resource.Loading())
                if (validate(email, password)) {
                    Firebase.auth.signInWithEmailAndPassword(email, password).await()
                    repository.setLoginData(context, email)
//                     val rle:Int=
                    if(role==0)
                        _login.postValue(Resource.Success(data=0,message = "Login Successful"))
                    else
                        _login.postValue(Resource.Success(data=1,message = "Login Successful"))

                }
            } catch (e: Exception) {
                Log.d("poiu", e.localizedMessage)
                _login.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun signUpProcess(
        role: Int,
        name: String,
        usn: String,
        email: String,
        password: String,
        re_password: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                _signup.postValue((Resource.Loading()))
                signUpValidate(role, name, usn, email, password, re_password)
                Firebase.auth.createUserWithEmailAndPassword(email, password).await()
                Firebase.auth.currentUser?.sendEmailVerification()
                val uid = Utility.createID()
//                if (role == 0) {
//                    val teacherModel = TeacherModel(name, email, uid)
//                    repository.sendTeacherData(teacherModel)
//                } else {
//                    val studentModel = StudentModel(name, email, uid, usn)
//                    repository.sendStudentData(studentModel)
//                }
                if(usn.isNullOrEmpty())
                    repository.setSignUpData(context, email, role, name, null, uid)
                else
                    repository.setSignUpData(context, email, role, name, usn, uid)
//                repository.showAlertDialog()
              _signup.postValue((Resource.Success(data =0,message = "Registered")))
            } catch (e: Exception) {
                _signup.postValue(Resource.Error(message = "" + e.localizedMessage))
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

    private fun signUpValidate(
        role: Int,
        name: String,
        usn: String,
        email: String,
        password: String,
        re_password: String
    ) {
        val validation =
            email.isEmpty() || password.isEmpty() || re_password.isEmpty() || name.isEmpty()
        if ((role == 0 && validation) || (role == 1 && (validation || usn.isEmpty())))
            throw Exception("Enter all fields")
        else if (!email.matches(Regex("[a-zA-Z]+[._A-Za-z0-9]*[@][a-zA-Z]+[.][a-zA-Z]+")))
            throw Exception("Invalid Email")
        else if (role == 1 && !usn.matches(Regex("[1][Dd][Ss][1-9][0-9][A-Za-z][A-Za-z][0-9][0-9][0-9]")))
            throw Exception("Invalid USN")
        else if (password != re_password)
            throw Exception("Passwords must match")
    }
}