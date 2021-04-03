package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Adapters.ClassroomAdapter
import com.dettoapp.detto.TeacherActivity.Adapters.ProjectAdapterClassroomDetail
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentClassroomProjectsBinding

class ClassroomProjectsFragment(private val operations: ClassroomDetailOperations) : Fragment() {
    private lateinit var projectAdapterClassroomDetail: ProjectAdapterClassroomDetail

    private val binding:FragmentClassroomProjectsBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classroom_projects, container, false)
    }

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
                    (requireActivity() as BaseActivity).showErrorSnackMessage(it.message!!)
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