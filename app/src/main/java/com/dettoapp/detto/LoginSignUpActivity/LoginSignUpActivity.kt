package com.dettoapp.detto.LoginSignUpActivity

import android.content.Intent
import android.os.Bundle
import com.dettoapp.detto.LoginSignUpActivity.Fragments.LoginFrag
import com.dettoapp.detto.LoginSignUpActivity.Fragments.SignUpFrag
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.TeacherActivity
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Utility
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginSignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sign_up)

        if(Firebase.auth.uid==null)
        Utility.navigateFragment(supportFragmentManager,R.id.loginFragContainer, LoginFrag(),"splash",addToBackStack = false)
        else
        {
            startActivity(Intent(this,TeacherActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }
    }
}
