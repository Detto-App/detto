package com.dettoapp.detto.clean_architecture.domain.use_case

import com.dettoapp.detto.Models.ReceivingUserModel
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.UtilityClasses.Utility.toLowerAndTrim
import com.dettoapp.detto.clean_architecture.domain.repository.LoginSignUpComposeRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginUsersUseCase @Inject constructor(
    private val loginSignUpComposeRepository: LoginSignUpComposeRepository
) {
    operator fun invoke(role: Int, email: String, password: String) = flow<Resource<Int>> {
        try {
            emit(Resource.Loading())
            val emailLocal = email.toLowerAndTrim()

            if (validate(emailLocal, password, role)) {

                Firebase.auth.signInWithEmailAndPassword(emailLocal, password).await()
                if (Firebase.auth.currentUser?.isEmailVerified == true) {

                    val receivingUserModel = loginSignUpComposeRepository.getUserDetailsFromServer(emailLocal, role)

                    storeUserDataInSharedPreferences(receivingUserModel)

                    loginSignUpComposeRepository.storeUserToken(receivingUserModel.token)
                    Utility.initialiseToken(receivingUserModel.token)

                    if (role == Constants.TEACHER) {
                        loginSignUpComposeRepository.getTeacherClassroomsDetailsAndStore(emailLocal)
                    } else if (role == Constants.STUDENT) {
                        loginSignUpComposeRepository.getStudentClassroomsDetailsAndStore(emailLocal)
                    }

                    emit(Resource.Success(data = role, message = "Registered"))
                } else {
                    emit(Resource.Error(message = "Please verify your email and login again"))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = ""+e.localizedMessage))
        }
    }

    private fun validate(email: String, password: String, role: Int): Boolean {
        if (email.isEmpty() || password.isEmpty())
            throw Exception(Constants.ERROR_FILL_ALL_FIELDS)
        else if (!email.matches(Regex("[a-zA-Z]+[-._A-Za-z0-9]*[@][a-zA-Z]+[.a-zA-Z]+")))
            throw Exception("Invalid Email")
        else if (role == -1)
            throw Exception("Please Select User role")
        return true
    }

    private fun storeUserDataInSharedPreferences(receivingUserModel: ReceivingUserModel) {
        if (receivingUserModel.teacher != null) {
            loginSignUpComposeRepository.storeTeacherModelInSharedPreferences(receivingUserModel.teacher)
        } else if (receivingUserModel.student != null) {
            loginSignUpComposeRepository.storeStudentModelInSharedPreferences(receivingUserModel.student)
        }
    }
}