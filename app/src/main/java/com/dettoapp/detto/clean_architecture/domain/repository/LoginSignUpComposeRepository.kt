package com.dettoapp.detto.clean_architecture.domain.repository

import com.dettoapp.detto.Models.ReceivingUserModel
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.Models.TeacherModel

interface LoginSignUpComposeRepository {
    fun getUserRole(): Int

    suspend fun getUserDetailsFromServer(email: String, role: Int): ReceivingUserModel

    fun storeTeacherModelInSharedPreferences(teacherModel: TeacherModel)

    fun storeStudentModelInSharedPreferences(studentModel: StudentModel)

    fun storeUserToken(token: String)

    suspend fun getTeacherClassroomsDetailsAndStore(email: String)

    suspend fun getStudentClassroomsDetailsAndStore(email: String)
}