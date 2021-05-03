package com.dettoapp.detto.StudentActivity

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.Fragments.StudentHomeFrag
import com.dettoapp.detto.StudentActivity.ViewModels.StudentSubmissionViewModel
import com.dettoapp.detto.UtilityClasses.*

class StudentActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
        Utility.navigateFragment(
                supportFragmentManager,
                R.id.StudentFragContainer,
                StudentHomeFrag(),
                "StudentHomeFrag",
                false,
                false
        )

    }
}