package com.dettoapp.detto.StudentActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.StudentActivity.Dialog.ProjectDetailsDialog
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentClassDetailViewModel
import com.dettoapp.detto.StudentActivity.ViewModels.StudentClassDetailViewModelFactory
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentStudentClassDetailsBinding


class StudentClassDetailsFrag(private val classroom: Classroom) : Fragment(), ProjectDetailsDialog.ProjectDialogClickListener {
    private var _binding: FragmentStudentClassDetailsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var pDialog: DialogFragment

    private val viewModel: StudentClassDetailViewModel by viewModels(factoryProducer =
    {
        StudentClassDetailViewModelFactory(
            StudentRepository(DatabaseDetto.getInstance(requireContext().applicationContext).classroomDAO,
                DatabaseDetto.getInstance(requireContext().applicationContext).projectDAO),
            requireContext().applicationContext
        )
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
        liveDataObservers()

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
        //binding.yesProjectContent.visibility = View.VISIBLE
        //pDialog.dismiss()
        //binding.noProjectContent.visibility = View.GONE
    }

    private fun liveDataObservers() {
        viewModel.stuViewModel.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
//                    (requireActivity() as BaseActivity).hideProgressBar()
//                    showAlertDialog("Verify Email", "A Verification Email has been sent to your email,Please Verify the email and Login Again")
                }
                is Resource.Error -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    (requireActivity() as BaseActivity).showProgressDialog(Constants.MESSAGE_LOADING)
                }
                else -> {
                }
            }
        })
    }
}