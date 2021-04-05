package com.dettoapp.detto.LoginSignUpActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.LoginSignUpActivity.Fragments.LoginFrag
import com.dettoapp.detto.LoginSignUpActivity.ViewModels.LoginSignUpActivityViewModelFactory
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.StudentActivity
import com.dettoapp.detto.TeacherActivity.TeacherActivity
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.loginActivity.ViewModels.LoginSignUpActivityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginSignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sign_up)
        val factory = LoginSignUpActivityViewModelFactory(
                LoginSignUpRepository(
                        DatabaseDetto.getInstance(this).classroomDAO
                ), this.applicationContext
        )
        val viewModel =
                ViewModelProvider(this, factory).get(LoginSignUpActivityViewModel::class.java)

        if (Firebase.auth.currentUser == null || Firebase.auth.uid == null || Firebase.auth.currentUser?.isEmailVerified == false)
            Utility.navigateFragment(
                    supportFragmentManager,
                    R.id.loginFragContainer,
                    LoginFrag(),
                    "splash",
                    addToBackStack = false
            )
        else {
            val role = viewModel.getRole()
            Utility.initialiseData(role, this.applicationContext)
            if (role == Constants.TEACHER)
                Utility.navigateActivity(this, TeacherActivity::class.java)
            else
                Utility.navigateActivity(this, StudentActivity::class.java)

        }
    }
}
