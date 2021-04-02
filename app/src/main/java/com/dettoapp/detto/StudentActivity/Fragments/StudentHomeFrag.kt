package com.dettoapp.detto.StudentActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.Adapters.StudentClassroomAdapter
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentHomeFragViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
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