package com.dettoapp.detto.TeacherActivity.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import androidx.core.content.ContentProviderCompat.requireContext
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Resource
import com.google.android.material.textfield.TextInputLayout

class GroupInfoDialog (private val contextInfo:Context,private val groupInfoDialogOnClickListener: GroupInfoDialogOnClickListener):Dialog(contextInfo,android.R.style.ThemeOverlay){


   interface GroupInfoDialogOnClickListener{
       fun onClassCreated(classname:String,sem:String,sec:String,teamSize:String,projectType:String)
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_classroomcreate)
        initialise()
        val classname=findViewById<TextInputLayout>(R.id.ed_classroomname)
        val sem =findViewById<AutoCompleteTextView>(R.id.semester)
        val sec =findViewById<AutoCompleteTextView>(R.id.section)
        val btn=findViewById<Button>(R.id.btn_createclassroom)
        val teamSize=findViewById<AutoCompleteTextView>(R.id.teamSize)
        val projectType=findViewById<AutoCompleteTextView>(R.id.ProjectType)
        btn.setOnClickListener {
            groupInfoDialogOnClickListener.onClassCreated(classname.editText?.text.toString(),sem.text.toString(),sec.text.toString(),teamSize.text.toString(),projectType.text.toString())

        }
    }
    private fun initialise(){
        val semester= findViewById<AutoCompleteTextView>(R.id.semester)
        val section=findViewById<AutoCompleteTextView>(R.id.section)
        val teamSize=findViewById<AutoCompleteTextView>(R.id.teamSize)
        val projectType=findViewById<AutoCompleteTextView>(R.id.ProjectType)

        val  semesterAdapter = ArrayAdapter(semester.context, android.R.layout.simple_spinner_dropdown_item, semester.context.resources.getStringArray(R.array.sem))
        semester.setAdapter(semesterAdapter)
        val  sectionAdapter = ArrayAdapter(section.context, android.R.layout.simple_spinner_dropdown_item, section.context.resources.getStringArray(R.array.sec))
        section.setAdapter(sectionAdapter)
        val  teamSizeAdapter = ArrayAdapter(teamSize.context, android.R.layout.simple_spinner_dropdown_item, teamSize.context.resources.getStringArray(R.array.teamSize))
        teamSize.setAdapter(teamSizeAdapter)
        val  projectTypeAdapter = ArrayAdapter(projectType.context, android.R.layout.simple_spinner_dropdown_item, projectType.context.resources.getStringArray(R.array.project_type))
        projectType.setAdapter(projectTypeAdapter)


    }



}