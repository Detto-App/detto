package com.dettoapp.detto.LoginSignUpActivity

import android.content.Context
import android.content.SharedPreferences
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.Models.TeacherModel
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility

@Suppress("SameParameterValue")
class LoginSignUpRepository {
    suspend fun sendTeacherData(teacherModel: TeacherModel) {
        RetrofitInstance.registrationAPI.sendTeacherData(teacherModel)
    }

    suspend fun sendStudentData(studentModel: StudentModel) {
        RetrofitInstance.registrationAPI.sendStudentData(studentModel)
    }

    suspend fun setLoginData(context: Context, email: String) {
//        val receivingUserModel = RetrofitInstance.registrationAPI.getDetails(email).body()!!
//        if (receivingUserModel.student != null)
//            storeDataInSharedPreferences(context, email, receivingUserModel.student.name, Constants.STUDENT, receivingUserModel.student.uid)
//        else
//            storeDataInSharedPreferences(context, email, receivingUserModel.teacher!!.name, Constants.TEACHER, receivingUserModel.teacher.uid)
    }

    fun setSignUpData(context: Context, email: String, role: Int, name: String, usn: String,userID: String)
    {
        storeDataInSharedPreferences(context,email,name,role,userID,usn)
    }

    private fun storeDataInSharedPreferences(context: Context, email: String, name: String, role: Int, userID: String,usn:String?=null) {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")

        with(sharedPreference.edit())
        {
            putString(Constants.USER_EMAIL_KEY, email)
            putString(Constants.USER_NAME_KEY, name)
            putInt(Constants.USER_ROLE_KEY, role)
            putString(Constants.USER_ID, userID)
            insertIfNotNull(this,Constants.USER_USN_KEY,usn)
            apply()
        }
    }

    private fun insertIfNotNull(sharedPreference:SharedPreferences.Editor,key:String,value:String?)
    {
        if(value!=null)
            sharedPreference.putString(key,value)
    }
}