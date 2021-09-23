package com.dettoapp.detto.clean_architecture.data.repository

import com.dettoapp.detto.clean_architecture.common.PreferenceManager
import com.dettoapp.detto.clean_architecture.domain.repository.LoginSignUpComposeRepository
import javax.inject.Inject

class LoginSignUpComposeRepositoryImpl @Inject constructor(
    private val userDetailsSharedPreference: PreferenceManager.UserDetailsSharedPreference
) : LoginSignUpComposeRepository {

    override fun getUserRole(): Int {
        return userDetailsSharedPreference.getUserRole()
    }
}