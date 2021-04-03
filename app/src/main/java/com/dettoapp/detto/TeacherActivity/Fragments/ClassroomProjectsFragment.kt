package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Adapters.ClassroomAdapter
import com.dettoapp.detto.TeacherActivity.Adapters.ProjectAdapterClassroomDetail
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentClassroomProjectsBinding

class ClassroomProjectsFragment(private val operations: ClassroomDetailOperations) :
    BaseFragment<ClassRoomDetailViewModel,FragmentClassroomProjectsBinding, ClassroomDetailRepository>() {
    private lateinit var projectAdapterClassroomDetail: ProjectAdapterClassroomDetail


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
    }

    private fun initialise() {
        projectAdapterClassroomDetail = ProjectAdapterClassroomDetail()
        binding.tClassroomProjectRecyclerview.apply {
            adapter = projectAdapterClassroomDetail
            layoutManager = LinearLayoutManager(requireContext())
        }
        operations.getProjects()
        liveDataObservers()
    }

    fun liveDataObservers(){
        operations.getViewModel().projectList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressBarInProject.visibility = View.GONE
                    projectAdapterClassroomDetail.differ.submitList(it.data)
                }
                is Resource.Error -> {
                    binding.progressBarInProject.visibility = View.GONE
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

    override fun getViewModelClass(): Class<ClassRoomDetailViewModel> {
        return ClassRoomDetailViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentClassroomProjectsBinding {
        return FragmentClassroomProjectsBinding.inflate(inflater,container,false)
    }

    override fun getRepository(): ClassroomDetailRepository {
        return ClassroomDetailRepository()
    }

    override fun getBaseViewModelOwner(): ViewModelStoreOwner {
        return operations.getViewModelStoreOwner()
    }
}