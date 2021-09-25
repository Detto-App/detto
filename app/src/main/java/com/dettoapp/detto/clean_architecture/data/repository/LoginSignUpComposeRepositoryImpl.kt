package com.dettoapp.detto.clean_architecture.data.repository

import com.dettoapp.detto.APIs.CreateClassroomAPI
import com.dettoapp.detto.APIs.RegistrationAPI
import com.dettoapp.detto.Db.ClassroomDAO
import com.dettoapp.detto.Models.ReceivingUserModel
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.Models.TeacherModel
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.clean_architecture.common.PreferenceManager
import com.dettoapp.detto.clean_architecture.domain.repository.LoginSignUpComposeRepository
import javax.inject.Inject

class LoginSignUpComposeRepositoryImpl @Inject constructor(
    private val userDetailsSharedPreference: PreferenceManager.UserDetailsSharedPreference,
    private val registrationAPI: RegistrationAPI,
    private val createClassroomAPI: CreateClassroomAPI,
    private val classroomDAO: ClassroomDAO
) : LoginSignUpComposeRepository {

    override fun getUserRole(): Int {
        return userDetailsSharedPreference.getUserRole()
    }

    override suspend fun getUserDetailsFromServer(email: String, role: Int): ReceivingUserModel {
        return registrationAPI.getDetails(email, role.toString()).body() ?: throw Exception("Unable to fetch data")
    }

    override fun storeTeacherModelInSharedPreferences(teacherModel: TeacherModel) {
        userDetailsSharedPreference.storeTeacherModel(teacherModel)
        userDetailsSharedPreference.storeTeacherModelDataAsJson(teacherModel)
    }

    override fun storeStudentModelInSharedPreferences(studentModel: StudentModel) {
        userDetailsSharedPreference.storeStudentModel(studentModel)
        userDetailsSharedPreference.storeStudentModelDataAsJson(studentModel)
    }

    override fun storeUserToken(token: String) {
        userDetailsSharedPreference.storeUserToken(token)
    }

    override suspend fun getTeacherClassroomsDetailsAndStore(email: String) {
        val classroomList = registrationAPI.getTeacherClassrooms(email, Utility.TOKEN).body()
            ?: throw Exception("Unable to fetch Teacher Data")
        classroomDAO.insertClassroom(classroomList)
    }

    override suspend fun getStudentClassroomsDetailsAndStore(email: String) {
        val classList = createClassroomAPI.getStudentClassroom(email, Utility.TOKEN).body()
            ?: throw Exception("Unable to Find Classrooms for the user")
        classroomDAO.insertClassroom(classList)
    }
}