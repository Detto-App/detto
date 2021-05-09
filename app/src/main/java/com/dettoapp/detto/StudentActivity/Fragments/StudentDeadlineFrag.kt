package com.dettoapp.detto.StudentActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.Adapters.StudentClassroomAdapter
import com.dettoapp.detto.StudentActivity.StudentOperations
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentClassDetailViewModel
import com.dettoapp.detto.TeacherActivity.Adapters.DeadlineAdapterClassroomDetail
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentDeadlineBinding


class StudentDeadlineFrag(private val classroom: Classroom,private val studentOperations: StudentOperations) :
    BaseFragment<StudentClassDetailViewModel, FragmentDeadlineBinding, StudentRepository>(),
    StudentClassroomAdapter.StudentClassroomAdapterCLickListener{

    private lateinit var deadlineAdapter: DeadlineAdapterClassroomDetail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()
    }

    private fun initialise(){
        binding.datePicker.visibility=View.GONE
        deadlineAdapter = DeadlineAdapterClassroomDetail()
        binding.deadlineRecyclerView.apply {
            adapter = deadlineAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.getDeadlineFromServer(classroom.classroomuid)

    }

    fun liveDataObservers(){
        viewModel.studentDeadline.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.prgressBarDeadline.visibility = View.GONE
//                    binding./.isRefreshing = false
                    deadlineAdapter.differ.submitList(it.data)

                }
                is Resource.Error -> {
                    baseActivity.showErrorSnackMessage(it.message!!)
                }
                is Resource.Loading -> {
                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
                }
                is Resource.Confirm ->{
                    baseActivity.hideProgressDialog()
                }
                else -> {
                }
            }
        })
    }

    override fun onViewHolderClick(classroom: Classroom) {
        Utility.navigateFragment(requireActivity().supportFragmentManager, R.id.StudentFragContainer,
            StudentClassDetailsFrag(classroom), "abcd", true)
    }


    override fun getViewModelClass(): Class<StudentClassDetailViewModel> {
        return StudentClassDetailViewModel::class.java
    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDeadlineBinding {
        return FragmentDeadlineBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): StudentRepository {
        return StudentRepository( DatabaseDetto.getInstance(requireContext().applicationContext).classroomDAO,
            DatabaseDetto.getInstance(requireContext().applicationContext).projectDAO)
    }

    override fun getBaseViewModelOwner(): ViewModelStoreOwner {
        return studentOperations.getViewModelOwner()
    }
}