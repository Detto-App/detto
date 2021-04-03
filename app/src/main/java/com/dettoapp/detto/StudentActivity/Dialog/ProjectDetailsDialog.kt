package com.dettoapp.detto.StudentActivity.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.Adapters.AddMembersAdapter
import com.dettoapp.detto.databinding.DialogProjectDetailsBinding

class ProjectDetailsDialog(private val projectDialogClickListener: ProjectDialogClickListener) :
        DialogFragment() {

    interface ProjectDialogClickListener {
        fun onProjectCreate(title: String, description: String, usnMap: HashMap<Int, String>, arrayList: ArrayList<String>)
        fun getClassroom(): Classroom
    }

    private val binding: DialogProjectDetailsBinding by viewBinding()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_project_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterLocal = AddMembersAdapter(projectDialogClickListener.getClassroom())
        binding.projectGrpRecyclerVIew.apply {
            adapter = adapterLocal
            layoutManager = LinearLayoutManager(view.context)
        }

        binding.addProjectUsn.setOnClickListener {
            adapterLocal.addOption()
        }

        binding.minusProjectUsn.setOnClickListener {
            adapterLocal.minusOption()
        }

        binding.doneProjectStudent.setOnClickListener {
            projectDialogClickListener.onProjectCreate(
                    binding.projectTitlePC.editText?.text.toString(),
                    binding.projectDescriptionPC.editText?.text.toString(),
                    adapterLocal.getUsnMap(),
                    adapterLocal.getArrayList()
            )
        }
    }

    override fun getTheme(): Int {
        return R.style.ThemeOverlay_MaterialComponents
    }

    fun getViewDialog() = binding.root
}