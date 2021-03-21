package com.dettoapp.detto.LoginSignUpActivity

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.DialogTitle
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.Models.TeacherModel
import com.dettoapp.detto.Models.Token
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility

@Suppress("SameParameterValue")
class LoginSignUpRepository {
    suspend fun sendTeacherData(context: Context): Token {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")

        val name=sharedPreference.getString(Constants.USER_NAME_KEY,"vishwa")!!
        val email=sharedPreference.getString(Constants.USER_EMAIL_KEY,"vishwda2@gmail.com")!!
        val uid=sharedPreference.getString(Constants.USER_ID,"1234")!!

        val teacherModel=TeacherModel(name,email,uid)
        val x = RetrofitInstance.registrationAPI.sendTeacherData(teacherModel)
        return x.body()!!

    }

    suspend fun sendStudentData(context: Context):Token {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        val name=sharedPreference.getString(Constants.USER_NAME_KEY,"")!!
        val email=sharedPreference.getString(Constants.USER_EMAIL_KEY,"")!!
        val uid=sharedPreference.getString(Constants.USER_ID,"")!!
        val usn=sharedPreference.getString(Constants.USER_USN_KEY,"")!!
        val studentModel=StudentModel(name,email,uid,usn)

        return RetrofitInstance.registrationAPI.sendStudentData(studentModel).body()!!
    }

    suspend fun setLoginData(context: Context, email: String) {
        val receivingUserModel = RetrofitInstance.registrationAPI.getDetails(email).body()!!
        if (receivingUserModel.student != null)
            storeDataInSharedPreferences(context, email, receivingUserModel.student.name, Constants.STUDENT, receivingUserModel.student.uid)
        else
            storeDataInSharedPreferences(context, email, receivingUserModel.teacher!!.name, Constants.TEACHER, receivingUserModel.teacher.uid)
}

    fun setSignUpData(context: Context, email: String, role: Int, name: String, usn: String?,userID: String)
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
    fun getRole(context: Context):Int{
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        val role=sharedPreference.getInt("role",-1)
        return role

    }



    private fun insertIfNotNull(sharedPreference:SharedPreferences.Editor,key:String,value:String?)
    {
        if(value!=null)
            sharedPreference.putString(key,value)
    }
    fun saveToken(token:Token,context: Context){
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        with(sharedPreference.edit()){
            putString(Constants.USER_TOKEN_KEY,token.token)
            apply()
        }
        Log.d("abcd",token.token)
    }
}