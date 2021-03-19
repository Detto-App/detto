package com.dettoapp.detto.LoginSignUpActivity

import android.content.Context
import android.util.Log
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.Models.TeacherModel
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.RetrofitInstance

class LoginSignUpRepository {
    suspend fun sendTeacherData(teacherModel: TeacherModel){
        RetrofitInstance.registrationAPI.sendTeacherData(teacherModel)
    }
    suspend fun sendStudentData(studentModel: StudentModel){
        RetrofitInstance.registrationAPI.sendStudentData(studentModel)
    }
    fun setEmailInSharedPrefrences(context:Context,email:String){
        val sharedPref = context?.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(Constants.USER_EMAIL_KEY, email)
            apply()
//            Log.d("abcd",email)
        }
    }
    fun setSignUpDataInSharedPrefrences(context:Context,email:String,role:Int,name:String,usn:String){
        val sharedPref = context?.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(Constants.USER_EMAIL_KEY, email)
            putString(Constants.USER_USN_KEY, usn)
            putString(Constants.USER_NAME_KEY, name)
            if(role==0)
                putInt(Constants.USER_ROLE_KEY,Constants.TEACHER)
            else
                putInt(Constants.USER_ROLE_KEY,Constants.STUDENT)
            apply()
            Log.d("abcd","details"+email+""+usn+""+name+""+role)
        }
    }
}