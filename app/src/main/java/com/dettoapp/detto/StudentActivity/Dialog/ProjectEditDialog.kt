package com.dettoapp.detto.StudentActivity.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.DialogFragment
import com.dettoapp.detto.R
import com.dettoapp.detto.databinding.DialogProjectEditBinding

class ProjectEditDialog(private val projectEditDialogClickListener: ProjectEditDialogClickListner) : DialogFragment() {
    interface ProjectEditDialogClickListner {
        fun onProjectEdit(title: String, description: String)
    }

    private val binding: DialogProjectEditBinding by viewBinding()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_project_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.reVerification.setOnClickListener {
            projectEditDialogClickListener.onProjectEdit(
                binding.projectname.editText?.text.toString(),
                binding.projectdetails.editText?.text.toString()
            )
        }
    }
}

