package com.dettoapp.detto.StudentActivity

import android.os.Bundle
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.Fragments.StudentHomeFragStudentClassroom
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Utility

class StudentActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        Utility.navigateFragment(supportFragmentManager,R.id.StudentFragContainer, StudentHomeFragStudentClassroom(),"StudentHomeFrag",false,false)

    }
}