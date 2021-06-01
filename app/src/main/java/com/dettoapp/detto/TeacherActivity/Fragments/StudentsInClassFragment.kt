package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.TeacherActivity.Adapters.StudentsAdapterClassRoomDetail
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentStudentsInClassBinding


class StudentsInClassFragment(private val classroomDetailOperations: ClassroomDetailOperations) :
    BaseFragment<ClassRoomDetailViewModel, FragmentStudentsInClassBinding, ClassroomDetailRepository>() {


    private lateinit var studentsAdapter: StudentsAdapterClassRoomDetail


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setUpLiveDataObservers()

    }

    private fun setUpLiveDataObservers() {
        viewModel.classroomStudents.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressBarStudentSV.visibility = View.GONE
                    binding.swipeToRefreshClassroomDetail.isRefreshing = false
                    studentsAdapter.differ.submitList(it.data)
                }
                is Resource.Error -> {
                    binding.progressBarStudentSV.visibility = View.GONE
                    binding.swipeToRefreshClassroomDetail.isRefreshing = false
                    baseActivity.showErrorSnackMessage(it.message!!)
                }
                is Resource.Loading -> {
                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
                }
                else -> {
                }
            }
        })


    }

    private fun initialise() {
        studentsAdapter = StudentsAdapterClassRoomDetail()
        binding.recyclerViewStudentsInClass.apply {
            adapter = studentsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.swipeToRefreshClassroomDetail.setOnRefreshListener {
            classroomDetailOperations.getClassroomStudents()
            binding.swipeToRefreshClassroomDetail.isRefreshing = true
        }

        classroomDetailOperations.getClassroomStudents()
    }

    override fun getViewModelClass(): Class<ClassRoomDetailViewModel> {
        return ClassRoomDetailViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStudentsInClassBinding {
        return FragmentStudentsInClassBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): ClassroomDetailRepository {
        return ClassroomDetailRepository(DatabaseDetto.getInstance(requireContext().applicationContext).rubricsDAO)
    }

    override fun getBaseViewModelOwner(): ViewModelStoreOwner {
        return classroomDetailOperations.getViewModelStoreOwner()
    }


}