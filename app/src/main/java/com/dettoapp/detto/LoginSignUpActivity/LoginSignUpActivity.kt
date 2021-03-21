package com.dettoapp.detto.LoginSignUpActivity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.LoginSignUpActivity.Fragments.LoginFrag
import com.dettoapp.detto.LoginSignUpActivity.Fragments.SignUpFrag
import com.dettoapp.detto.LoginSignUpActivity.ViewModels.LoginSignUpActivityViewModelFactory
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.StudentActivity
import com.dettoapp.detto.TeacherActivity.Fragments.TeacherHomeFrag
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
        val factory = LoginSignUpActivityViewModelFactory(LoginSignUpRepository(), this.applicationContext)
        val viewModel = ViewModelProvider(this, factory).get(LoginSignUpActivityViewModel::class.java)

        if (Firebase.auth.currentUser == null || Firebase.auth.uid == null || Firebase.auth.currentUser?.isEmailVerified==false)
            Utility.navigateFragment(supportFragmentManager, R.id.loginFragContainer, LoginFrag(), "splash", addToBackStack = false)
        else {
            val role = viewModel.getRole()
            val intent = if (role == Constants.TEACHER)
                Intent(this, TeacherActivity::class.java)
            else
                Intent(this, StudentActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent)
        }

//        if((Firebase.auth.currentUser?.isEmailVerified == false))
//
//        else
//        {
//            startActivity(Intent(this,TeacherActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            })
//        }
    }
}
