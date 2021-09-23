package com.dettoapp.detto.clean_architecture.domain.use_case

import com.dettoapp.detto.clean_architecture.domain.repository.LoginSignUpComposeRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VerifyUserLoginUseCase @Inject constructor(
    private val loginSignUpComposeRepository: LoginSignUpComposeRepository
) {
    operator fun invoke(): Flow<Int> = flow {
        if (Firebase.auth.currentUser == null || Firebase.auth.uid == null
            || Firebase.auth.currentUser?.isEmailVerified == false
        ) {
            //Here -1 event signifies that the user has not logged in
            emit(-1)
        } else {
            emit(loginSignUpComposeRepository.getUserRole())
        }
    }

}