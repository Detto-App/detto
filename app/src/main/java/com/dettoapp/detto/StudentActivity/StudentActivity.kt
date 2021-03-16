package com.dettoapp.detto.StudentActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.BaseActivity

class StudentActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
    }
}