package com.dettoapp.detto.LoginSignUpActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dettoapp.detto.LoginSignUpActivity.Fragments.LoginFrag
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Utility

class LoginSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sign_up)

        Utility.navigateFragment(supportFragmentManager,R.id.loginFragContainer, LoginFrag(),"splash",addToBackStack = false)

    }
}
