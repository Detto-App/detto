package com.dettoapp.detto.LinkParseActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dettoapp.detto.LoginSignUpActivity.Fragments.LoginFrag
import com.dettoapp.detto.LoginSignUpActivity.LoginSignUpActivity
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.StudentActivity
import com.dettoapp.detto.TeacherActivity.TeacherActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Utility
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LinkParseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_parse)

        if (Firebase.auth.currentUser == null || Firebase.auth.uid == null || Firebase.auth.currentUser?.isEmailVerified==false)
            startActivity(Intent(this,LoginSignUpActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        else {
            val role = getRole(this)
            val intent = if (role == Constants.TEACHER)
                Intent(this, TeacherActivity::class.java)
            else
                Intent(this, StudentActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            //startActivity(intent)
        }

        //finish()

        val incomingIntent = intent
        val data = incomingIntent.data
        Toast.makeText(this, ""+data, Toast.LENGTH_SHORT).show();
    }

    private fun getRole(context: Context): Int {
        val sharedPreference = context.getSharedPreferences(Constants.USER_DETAILS_FILE, Context.MODE_PRIVATE)
                ?: throw Exception("Data Storage Exception")
        return sharedPreference.getInt("role", -1)

    }
}