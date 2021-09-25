package com.dettoapp.detto.clean_architecture.common

import android.content.Context
import android.content.SharedPreferences
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.Models.TeacherModel
import com.dettoapp.detto.UtilityClasses.Constants
import com.google.gson.Gson

abstract class PreferenceManager(protected val context: Context)
{
    abstract val sharedPreference:SharedPreferences

    class UserDetailsSharedPreference(context: Context) : PreferenceManager(context)
    {
        override val sharedPreference: SharedPreferences
            get() = context.getSharedPreferences(Constants2.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")

        fun getUserRole() = sharedPreference.getInt(Constants2.PARAM_ROLE,-1)

        fun storeTeacherModel(teacherModel: TeacherModel)
        {
            with(sharedPreference.edit())
            {
                putString(Constants.USER_EMAIL_KEY, teacherModel.email)
                putString(Constants.USER_NAME_KEY, teacherModel.name)
                putInt(Constants.USER_ROLE_KEY, Constants2.USER_TEACHER)
                putString(Constants.USER_ID, teacherModel.uid)
                putString(Constants.ACCESS, "Teacher")

                apply()
            }
        }

        fun storeTeacherModelDataAsJson(teacherModel: TeacherModel)
        {
            with(sharedPreference.edit())
            {
                putString(Constants.ENTIRE_MODEL_KEY, Gson().toJson(teacherModel))
                apply()
            }
        }

        fun storeStudentModel(studentModel: StudentModel)
        {
            with(sharedPreference.edit())
            {
                putString(Constants.USER_EMAIL_KEY, studentModel.email)
                putString(Constants.USER_NAME_KEY, studentModel.name)
                putInt(Constants.USER_ROLE_KEY, Constants2.USER_STUDENT)
                putString(Constants.USER_ID, studentModel.uid)
                putString(Constants.ACCESS, "Student")
                putString(Constants.USER_USN_KEY, studentModel.susn)

                apply()
            }
        }

        fun storeStudentModelDataAsJson(studentModel: StudentModel)
        {
            with(sharedPreference.edit())
            {
                putString(Constants.ENTIRE_MODEL_KEY, Gson().toJson(studentModel))
                apply()
            }
        }

        fun storeUserToken(token:String)
        {
            with(sharedPreference.edit()) {
                putString(Constants.USER_TOKEN_KEY, token)
                apply()
            }
        }
    }
}