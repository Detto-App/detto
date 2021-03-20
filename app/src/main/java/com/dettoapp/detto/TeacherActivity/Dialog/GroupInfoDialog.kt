package com.dettoapp.detto.TeacherActivity.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.core.content.ContentProviderCompat.requireContext
import com.dettoapp.detto.R

class GroupInfoDialog (private val contextInfo:Context,private val groupInfoDialogOnClickListener: GroupInfoDialogOnClickListener):Dialog(contextInfo,android.R.style.ThemeOverlay){

   interface GroupInfoDialogOnClickListener{
       fun onClassCreated(classname:String,sem:String,sec:String)
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_classroomcreate)
        initialise()
        val classname=findViewById<EditText>(R.id.ed_classroomname)
        val sem =findViewById<Spinner>(R.id.year)
        val sec =findViewById<Spinner>(R.id.section)
        val btn=findViewById<Button>(R.id.btn_createclassroom)
        btn.setOnClickListener {
            groupInfoDialogOnClickListener.onClassCreated(classname.text.toString(),sem.selectedItem.toString(),sec.selectedItem.toString())
            dismiss()
        }
    }
    private fun initialise(){
        val semester= findViewById<Spinner>(R.id.year)
        val section=findViewById<Spinner>(R.id.section)
        semester.apply {
            adapter = ArrayAdapter(
                    contextInfo,
                    android.R.layout.simple_spinner_item, contextInfo.resources.getStringArray(R.array.sem)
            ).apply {
                setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            }
        }
        section.apply {
            adapter = ArrayAdapter(
                    contextInfo,
                    android.R.layout.simple_spinner_item, contextInfo.resources.getStringArray(R.array.sec)
            ).apply {
                setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            }
        }

    }



}