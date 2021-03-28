package com.dettoapp.detto.LoginSignUpActivity

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.dettoapp.detto.Db.ClassroomDAO
import com.dettoapp.detto.Models.*
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import retrofit2.Response

@Suppress("SameParameterValue")
class LoginSignUpRepository (private val dao: ClassroomDAO){
    suspend fun sendTeacherDataToServer(teacherModel: TeacherModel): Token {
        return RetrofitInstance.registrationAPI.sendTeacherData(teacherModel).body()!!
    }

    suspend fun sendStudentDataToServer(studentModel: StudentModel): Token {
        return RetrofitInstance.registrationAPI.sendStudentData(studentModel).body()!!
    }



    private fun storeDataInSharedPreferences(context: Context, email: String, name: String, role: Int, userID: String, usn: String? = null) {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")

        with(sharedPreference.edit())
        {
            putString(Constants.USER_EMAIL_KEY, email)
            putString(Constants.USER_NAME_KEY, name)
            putInt(Constants.USER_ROLE_KEY, role)
            putString(Constants.USER_ID, userID)
            insertIfNotNull(this, Constants.USER_USN_KEY, usn)
            apply()
        }
    }

    fun storeUserAndTokenData(context: Context, receivingUserModel: ReceivingUserModel) {
        if (receivingUserModel.teacher != null) {
            storeDataInSharedPreferences(context, receivingUserModel.teacher.email, receivingUserModel.teacher.name, Constants.TEACHER, receivingUserModel.teacher.uid)
        } else
            storeDataInSharedPreferences(context, receivingUserModel.student!!.email, receivingUserModel.student.name, Constants.STUDENT, receivingUserModel.student.uid, receivingUserModel.student.susn)
        saveToken(Token(receivingUserModel.token), context)
    }

    fun getRole(context: Context): Int {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        return sharedPreference.getInt("role", -1)!!

    }


    private fun insertIfNotNull(sharedPreference: SharedPreferences.Editor, key: String, value: String?) {
        if (value != null)
            sharedPreference.putString(key, value)
    }

    fun saveToken(token: Token, context: Context) {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        with(sharedPreference.edit()) {
            putString(Constants.USER_TOKEN_KEY, token.token)
            apply()
        }
        Log.d("abcd", token.token)
    }
    suspend fun getStudentClassroomsAndStore(email:String, context:Context){
        val classList = RetrofitInstance.createClassroomAPI.getStudentClassroom(email, Utility.gettoken(context)).body()?:
            throw Exception("Unable to Find Classrooms for the user")

        //dao.insertClassroom()

    }
    suspend fun insertClassroom(classroom: Classroom){
        dao.insertClassroom(classroom)
    }
}

//    fun setSignUpData(context: Context, email: String, role: Int, name: String, usn: String?, userID: String) {
//        storeDataInSharedPreferences(context, email, name, role, userID, usn)
//    }

//    suspend fun setLoginData(context: Context, email: String) {
//        val receivingUserModel = RetrofitInstance.registrationAPI.getDetails(email,1).body()!!
//        if (receivingUserModel.student != null)
//            storeDataInSharedPreferences(context, email, receivingUserModel.student.name, Constants.STUDENT, receivingUserModel.student.uid)
//        else
//            storeDataInSharedPreferences(context, email, receivingUserModel.teacher!!.name, Constants.TEACHER, receivingUserModel.teacher.uid)
//}