package com.dettoapp.detto.StudentActivity.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.DialogFragment
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Dialog.GroupInfoDialog

class ProjectDetailsDialog(): DialogFragment(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_project_details,container,false)
    }

    override fun getTheme(): Int {
        return R.style.ThemeOverlay_MaterialComponents
    }
}