package com.dettoapp.detto.clean_architecture.presentation.loginSignUpActivityCompose

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.clean_architecture.common.Constants2
import com.dettoapp.detto.clean_architecture.domain.use_case.LoginUsersUseCase
import com.dettoapp.detto.clean_architecture.domain.use_case.VerifyUserLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class LoginSignUpActivityComposeViewModel @Inject constructor(
    private val verifyUserLoginUseCase: VerifyUserLoginUseCase,
    private val loginUsersUseCase: LoginUsersUseCase
) : ViewModel() {

    private val _userRoleState = mutableStateOf(-1)
    val userRoleState: State<Int> = _userRoleState

//    private val _loginUsersState : MutableState<Resource<Int>> = mutableStateOf(Resource.Confirm(message = ""))
//    val loginUsersState : State<Resource<Int>> = _loginUsersState

    var loginUsersState = mutableStateOf<Resource<Int>>(Resource.Confirm(message = ""))

    init {
        getUser()
    }

    private fun getUser() {
        verifyUserLoginUseCase().onEach { userRole ->
            delay(1000)
            _userRoleState.value = userRole
        }.launchIn(viewModelScope + Dispatchers.IO)
    }

    fun loginUsers(role: String, email: String, password: String)
    {
        val roleLocal = if(role =="Student") Constants2.USER_STUDENT else Constants2.USER_TEACHER

        loginUsersUseCase(roleLocal,email, password).onEach {
            loginUsersState.value = it
        }.launchIn(viewModelScope+Dispatchers.IO)
    }
}