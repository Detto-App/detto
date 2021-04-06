package com.dettoapp.detto.TeacherActivity.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.Fragment
import com.dettoapp.detto.R
import com.google.android.material.textfield.TextInputLayout

class ClassroomCreateFragment(private val classroomCreateFragmentOnClickListener: ClassroomCreateFragmentOnClickListener) : Fragment() {


    interface ClassroomCreateFragmentOnClickListener {
        fun onClassCreated(classname: String, sem: String, sec: String, teamSize: String, projectType: String,groupType:String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_classroom_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise(view)
    }

    private fun initialise(view: View) {

        val classname = view.findViewById<TextInputLayout>(R.id.ed_classroomname)
        val btn = view.findViewById<Button>(R.id.btn_createclassroom)
        val semester = view.findViewById<AutoCompleteTextView>(R.id.semester)
        val section = view.findViewById<AutoCompleteTextView>(R.id.section)
        val teamSize = view.findViewById<AutoCompleteTextView>(R.id.teamSize)
        val projectType = view.findViewById<AutoCompleteTextView>(R.id.ProjectType)
        val groupType = view.findViewById<AutoCompleteTextView>(R.id.groupFormationType)

        val semesterAdapter = ArrayAdapter(semester.context, android.R.layout.simple_spinner_dropdown_item, semester.context.resources.getStringArray(R.array.sem))
        val sectionAdapter = ArrayAdapter(section.context, android.R.layout.simple_spinner_dropdown_item, section.context.resources.getStringArray(R.array.sec))
        val teamSizeAdapter = ArrayAdapter(teamSize.context, android.R.layout.simple_spinner_dropdown_item, teamSize.context.resources.getStringArray(R.array.teamSize))
        val projectTypeAdapter = ArrayAdapter(projectType.context, android.R.layout.simple_spinner_dropdown_item, projectType.context.resources.getStringArray(R.array.project_type))
        val groupTypeAdapter = ArrayAdapter(groupType.context, android.R.layout.simple_spinner_dropdown_item, groupType.context.resources.getStringArray(R.array.group_type))


        semester.setAdapter(semesterAdapter)
        section.setAdapter(sectionAdapter)
        teamSize.setAdapter(teamSizeAdapter)
        projectType.setAdapter(projectTypeAdapter)
        groupType.setAdapter(groupTypeAdapter)


        btn.setOnClickListener {
            classroomCreateFragmentOnClickListener.onClassCreated(classname.editText?.text.toString(), semester.text.toString(), section.text.toString(), teamSize.text.toString(), projectType.text.toString(),groupType.text.toString())
        }
    }

    fun getViewDialog(): View = requireView().findViewById(R.id.rootClassDialog)

}