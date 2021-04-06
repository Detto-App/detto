package com.dettoapp.detto.StudentActivity.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.Adapters.StudentClassroomAdapter
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentHomeFragViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentStudentHomeBinding


class StudentHomeFrag : BaseFragment<StudentHomeFragViewModel, FragmentStudentHomeBinding, StudentRepository>(), StudentClassroomAdapter.StudentClassroomAdapterCLickListener {

    private lateinit var studentClassroomAdapter: StudentClassroomAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()
    }

    private fun initialise() {
        studentClassroomAdapter = StudentClassroomAdapter(this)
        binding.studentRecyclerView.apply {
            adapter = studentClassroomAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun liveDataObservers() {
        viewModel.allClassRooms.observe(viewLifecycleOwner, Observer {
            studentClassroomAdapter.differ.submitList(it)
        })
        viewModel.project1.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
                }
                is Resource.Confirm ->{
                    showConfirmationDialog( "old projects found  \n press \"Yes\" to fetch")
                }
                is Resource.Success ->{
                    baseActivity.hideProgressDialog()
                    showToast("Successfully fetched all data")
                }
            }
        })
    }

    private fun showConfirmationDialog( dialogMessage: String) {

        val builder = AlertDialog.Builder(requireActivity())

        with(builder)
        {
            setTitle("Alert")
            setMessage(dialogMessage)
            setPositiveButton("Yes") { _, _ ->
                viewModel.download()
            }
            setNegativeButton("Cancel") { _, _ ->
                requireActivity().finish()
            }
        }
        val alertDialog: AlertDialog = builder.create().apply {
            setCancelable(false)
        }
        alertDialog.show()
    }

    fun showToast(message :String)
    {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onViewHolderClick(classroom: Classroom) {
        Utility.navigateFragment(requireActivity().supportFragmentManager, R.id.StudentFragContainer,
                StudentClassDetailsFrag(classroom), "abcd", true)

    }

    override fun getViewModelClass(): Class<StudentHomeFragViewModel> = StudentHomeFragViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentStudentHomeBinding = FragmentStudentHomeBinding.inflate(inflater, container, false)

    override fun getRepository(): StudentRepository = StudentRepository(
            DatabaseDetto.getInstance(requireContext()).classroomDAO,
            DatabaseDetto.getInstance(requireContext()).projectDAO)
}