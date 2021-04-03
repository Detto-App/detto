package com.dettoapp.detto.TeacherActivity.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import androidx.core.content.ContentProviderCompat.requireContext
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility.toStringLowerTrim
import com.google.android.material.textfield.TextInputLayout

class GroupInfoDialog(private val contextInfo: Context, private val groupInfoDialogOnClickListener: GroupInfoDialogOnClickListener) : Dialog(contextInfo, android.R.style.ThemeOverlay) {


    private lateinit var view: View

    interface GroupInfoDialogOnClickListener {
        fun onClassCreated(classname: String, sem: String, sec: String, teamSize: String, projectType: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = LayoutInflater.from(contextInfo).inflate(R.layout.dialog_classroomcreate, null)
        setContentView(view)
        initialise()

    }

    private fun initialise() {

        val classname = findViewById<TextInputLayout>(R.id.ed_classroomname)
        val btn = findViewById<Button>(R.id.btn_createclassroom)
        val semester = findViewById<AutoCompleteTextView>(R.id.semester)
        val section = findViewById<AutoCompleteTextView>(R.id.section)
        val teamSize = findViewById<AutoCompleteTextView>(R.id.teamSize)
        val projectType = findViewById<AutoCompleteTextView>(R.id.ProjectType)

        val semesterAdapter = ArrayAdapter(semester.context, android.R.layout.simple_spinner_dropdown_item, semester.context.resources.getStringArray(R.array.sem))
        val sectionAdapter = ArrayAdapter(section.context, android.R.layout.simple_spinner_dropdown_item, section.context.resources.getStringArray(R.array.sec))
        val teamSizeAdapter = ArrayAdapter(teamSize.context, android.R.layout.simple_spinner_dropdown_item, teamSize.context.resources.getStringArray(R.array.teamSize))
        val projectTypeAdapter = ArrayAdapter(projectType.context, android.R.layout.simple_spinner_dropdown_item, projectType.context.resources.getStringArray(R.array.project_type))

        semester.setAdapter(semesterAdapter)
        section.setAdapter(sectionAdapter)
        teamSize.setAdapter(teamSizeAdapter)
        projectType.setAdapter(projectTypeAdapter)


        btn.setOnClickListener {
            groupInfoDialogOnClickListener.onClassCreated(classname.editText?.text.toString(), semester.text.toString(), section.text.toString(), teamSize.text.toString(), projectType.text.toString())
        }
    }

    fun getViewDialog(): View = findViewById(R.id.rootClassDialog)
}