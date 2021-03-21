package com.dettoapp.detto.LoginSignUpActivity.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.LoginSignUpActivity.LoginSignUpRepository
import com.dettoapp.detto.loginActivity.ViewModels.LoginSignUpActivityViewModel

class LoginSignUpActivityViewModelFactory(private val repository: LoginSignUpRepository,private val context: Context) :ViewModelProvider.NewInstanceFactory()  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginSignUpActivityViewModel(repository,context) as T
    }
}