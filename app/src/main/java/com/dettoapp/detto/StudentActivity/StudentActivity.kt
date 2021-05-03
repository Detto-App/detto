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

//    val x : EasyPermission by requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, granted = {
//        Log.d("DDSS", "From Activity")
//    })

    val x: EasyPermission by requestMultiplePermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, granted = {
        Log.d("DDSS", "Heyyy")
    })
    //val x3 : EasyPermission by ActivityDelegateSingle(Manifest.permission.READ_EXTERNAL_STORAGE,{},{},{})

    val d: StudentSubmissionViewModel by viewModels()

    //val x2 : ActivityStudentBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        //Log.d("DDSS", "" + x.checkAndLaunch())
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