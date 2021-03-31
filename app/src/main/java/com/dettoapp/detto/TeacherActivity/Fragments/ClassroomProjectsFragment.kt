package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Adapters.ClassroomAdapter
import com.dettoapp.detto.TeacherActivity.Adapters.ProjectAdapterClassroomDetail
import com.dettoapp.detto.databinding.FragmentClassroomProjectsBinding

class ClassroomProjectsFragment : Fragment() {
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
    }
}