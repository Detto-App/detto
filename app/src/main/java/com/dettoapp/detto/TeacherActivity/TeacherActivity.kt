package com.dettoapp.detto.TeacherActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Fragments.TeacherHomeFrag
import com.dettoapp.detto.UtilityClasses.Utility

class TeacherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        Utility.navigateFragment(supportFragmentManager,R.id.teacherHomeContainer,TeacherHomeFrag(),"teacherHomeFrag")
    }
}