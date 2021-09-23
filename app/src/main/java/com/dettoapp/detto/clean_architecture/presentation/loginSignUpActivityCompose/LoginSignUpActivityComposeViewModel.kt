package com.dettoapp.detto.clean_architecture.presentation.loginSignUpActivityCompose

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.UtilityClasses.Resource
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
    private val verifyUserLoginUseCase: VerifyUserLoginUseCase
) : ViewModel() {

    private val _userRoleState = mutableStateOf(-1)
    val userRoleState: State<Int> = _userRoleState

    init {
        getUser()
    }

    private fun getUser() {
        verifyUserLoginUseCase().onEach { userRole ->
            delay(1000)
            _userRoleState.value = userRole
        }.launchIn(viewModelScope + Dispatchers.IO)
    }
}