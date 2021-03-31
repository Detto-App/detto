package com.dettoapp.detto.StudentActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.dettoapp.detto.Db.ClassroomDatabase
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.StudentActivity.Dialog.ProjectDetailsDialog
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentClassDetailViewModel
import com.dettoapp.detto.StudentActivity.ViewModels.StudentClassDetailViewModelFactory
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.databinding.FragmentStudentClassDetailsBinding


class StudentClassDetailsFrag(private val classroom: Classroom) : Fragment(), ProjectDetailsDialog.ProjectDialogClickListener {
    private var _binding: FragmentStudentClassDetailsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var pDialog:DialogFragment

    private val viewModel: StudentClassDetailViewModel by viewModels(factoryProducer =
    {
        StudentClassDetailViewModelFactory(
                StudentRepository(ClassroomDatabase.getInstance(requireContext().applicationContext).classroomDAO),
                requireContext().applicationContext)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStudentClassDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise(view)

    }

    private fun initialise(view: View) {

        binding.stuClassDetailsbutton.setOnClickListener {
            pDialog = ProjectDetailsDialog(this)
            pDialog.show(requireActivity().supportFragmentManager, "jdslfj")
        }
        view.setOnClickListener { }

        if (viewModel.getProjectFromSharedPref(classroom) == Constants.PROJECT_NOT_CREATED)
            binding.noProjectContent.visibility = View.VISIBLE
        else
            binding.yesProjectContent.visibility = View.VISIBLE


    }

    override fun onProjectCreate(title: String, description: String, usnMap: HashMap<Int, String>) {

        viewModel.storeProject(title, description, usnMap, classroom)
        binding.yesProjectContent.visibility = View.VISIBLE
        pDialog.dismiss()
        binding.noProjectContent.visibility = View.GONE
    }
}