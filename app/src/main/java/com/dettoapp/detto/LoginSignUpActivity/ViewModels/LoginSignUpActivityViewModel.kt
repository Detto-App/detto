package com.dettoapp.detto.loginActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.LoginSignUpActivity.LoginSignUpRepository
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


    private val _signUp = MutableLiveData<Resource<Int>>()
    val signUp: LiveData<Resource<Int>>
        get() = _signUp


    fun loginProcess(role: Int, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _login.postValue(Resource.Loading())
                if (validate(email, password)) {
                    Firebase.auth.signInWithEmailAndPassword(email, password).await()
                    if (Firebase.auth.currentUser?.isEmailVerified == true) {
                        val actualRole = repository.getRole(context)
                        if (role != actualRole) {
                            _login.postValue(Resource.Error(message = "Please Check Your User Role,Account Not Found"))
                            Firebase.auth.signOut()
                        } else
                            _login.postValue(Resource.Success(role, "Login Successful"))
                    } else {
                        _login.postValue(Resource.Error(message = "Please verify your email and login again"))
                    }
                }
            } catch (e: Exception) {
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
                _signUp.postValue((Resource.Loading()))
                signUpValidate(role, name, usn, email, password, re_password)
                Firebase.auth.createUserWithEmailAndPassword(email, password).await()
                Firebase.auth.currentUser?.sendEmailVerification()
                val uid = Utility.createID()

                if (usn.isEmpty())
                    repository.setSignUpData(context, email, role, name, null, uid)
                else
                    repository.setSignUpData(context, email, role, name, usn, uid)

                _signUp.postValue((Resource.Success(data = 0, message = "Registered")))

            } catch (e: Exception) {
                _signUp.postValue(Resource.Error(message = "" + e.localizedMessage))
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

    fun getRole(): Int = repository.getRole(context)

}