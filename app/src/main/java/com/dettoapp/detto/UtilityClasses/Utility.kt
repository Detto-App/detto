package com.dettoapp.detto.UtilityClasses

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.transition.Fade
import androidx.transition.Slide
import java.util.*

object Utility {
    private fun Fragment.applyTransaction(): Fragment {

        val startAnimation = Slide(Gravity.END)
        startAnimation.duration = 400L

        val endAnimation = Fade()
        endAnimation.duration = 200L

        this.enterTransition = startAnimation
        this.exitTransition = endAnimation

        return this
    }

    fun navigateFragment(fragManager: FragmentManager, containerID: Int, fragment: Fragment, tag: String, addToBackStack: Boolean = true, replaceOrAdd: Boolean = true) {

        val transaction = if (replaceOrAdd)
            fragManager.beginTransaction().add(containerID, fragment.applyTransaction())
        else
            fragManager.beginTransaction().replace(containerID, fragment.applyTransaction())

        if (addToBackStack)
            transaction.addToBackStack(tag)

        transaction.commit()

    }
    fun createID()=UUID.randomUUID().toString()

    fun gettoken(context: Context):String{

        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        return sharedPreference.getString(Constants.USER_TOKEN_KEY,"cc")!!
    }
    fun getTname(context: Context):String{
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
            ?: throw Exception("Data Storage Exception")
        return sharedPreference.getString(Constants.USER_NAME_KEY,"cc")!!

    }
}