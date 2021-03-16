package com.dettoapp.detto.TeacherActivity.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import com.dettoapp.detto.R

class GroupInfoDialog (context:Context):Dialog(context,android.R.style.ThemeOverlay){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_groupinfo)
        val btn=findViewById<Button>(R.id.btn_createclassroom)
        btn.setOnClickListener {
            dismiss()
        }
    }



}