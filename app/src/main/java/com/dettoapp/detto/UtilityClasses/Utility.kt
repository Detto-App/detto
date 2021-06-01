package com.dettoapp.detto.UtilityClasses

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.view.Gravity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.transition.Fade
import androidx.transition.Slide
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.Models.TeacherModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

object Utility {

    lateinit var TEACHER: TeacherModel
    lateinit var STUDENT: StudentModel
    lateinit var TOKEN: String
    lateinit var ACCESSARRAY:ArrayList<String>

    fun initialiseData(role: Int, appContext: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            val sharedPreference = appContext.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
            val dataInString = sharedPreference.getString(Constants.ENTIRE_MODEL_KEY, "")!!
            val accesArray=sharedPreference.getString(Constants.ACCESSARRAY,"")!!


            if (role == Constants.TEACHER) {
                val type = object : TypeToken<TeacherModel>() {}.type
                TEACHER = Gson().fromJson(dataInString, type)
            } else {
                val type = object : TypeToken<StudentModel>() {}.type
                STUDENT = Gson().fromJson(dataInString, type)
            }
            initialiseToken(context = appContext)
        }
    }

    fun initialiseToken(token: String? = null, context: Context? = null) {
        TOKEN = if (token != null)
            "Bearer $token"
        else {
            val sharedPreference = context?.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
            "Bearer " + sharedPreference.getString(Constants.USER_TOKEN_KEY, "cc")!!
        }
    }

    private fun Fragment.applyTransaction(): Fragment {

        val startAnimation = Slide(Gravity.END)
        startAnimation.duration = 400L

        val endAnimation = Fade()
        endAnimation.duration = 200L

        this.enterTransition = startAnimation
        this.exitTransition = endAnimation

        return this
    }

    fun navigateFragment(
        fragManager: FragmentManager,
        containerID: Int,
        fragment: Fragment,
        tag: String,
        addToBackStack: Boolean = true,
        isAdding: Boolean = true
    ) {

        val transaction = if (isAdding)
            fragManager.beginTransaction().add(containerID, fragment.applyTransaction())
        else
            fragManager.beginTransaction().replace(containerID, fragment.applyTransaction())

        if (addToBackStack)
            transaction.addToBackStack(tag)

        transaction.commit()

    }

    fun createID() = UUID.randomUUID().toString()

    @Deprecated("USE TOKEN variable")
    fun gettoken(context: Context): String {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
            ?: throw Exception("Data Storage Exception")
        return "Bearer " + sharedPreference.getString(Constants.USER_TOKEN_KEY, "cc")!!
    }


    @Deprecated("Use the STUDENT variable initialised by initialiseData() method")
    fun getStudentModel(context: Context): StudentModel {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
            ?: throw Exception("Data Storage Exception")

        return StudentModel(
            sharedPreference.getString(Constants.USER_NAME_KEY, "")!!,
            sharedPreference.getString(Constants.USER_EMAIL_KEY, "")!!,
            sharedPreference.getString(Constants.USER_ID, "")!!,
            sharedPreference.getString(Constants.USER_USN_KEY, "")!!,
            HashSet<String>()
        )


    }

    fun getUID(context: Context): String {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
            ?: throw Exception("Data Storage Exception")
        return sharedPreference.getString(Constants.USER_ID, "")!!
    }

    fun String.toLowerAndTrim() = this.trim().toLowerCase(Locale.ROOT)

    fun Editable?.toStringLowerTrim() = this.toString().toLowerAndTrim()

    fun HashMap<Int, String>.toHashSet(): HashSet<String> {
        val hashSet = HashSet<String>()

        for ((k, v) in this) {
            hashSet.add(v.toLowerAndTrim())
        }

        return hashSet
    }

    fun <T> navigateActivity(context: Context, toActivityClass: Class<T>) {
        val intent = Intent(context, toActivityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}