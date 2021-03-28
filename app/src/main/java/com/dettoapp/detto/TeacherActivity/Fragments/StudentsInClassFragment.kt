package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.TeacherActivity.Adapters.StudentsAdapterClassRoomDetail
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentStudentsInClassBinding


class StudentsInClassFragment(private val classroomDetailOperations: ClassroomDetailOperations) :
    Fragment() {

    private lateinit var viewModel: ClassRoomDetailViewModel
    private var _binding: FragmentStudentsInClassBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var studentsAdapter: StudentsAdapterClassRoomDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(requireParentFragment()).get(ClassRoomDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStudentsInClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initialise()
        setUpLiveDataObservers()

    }

    private fun setUpLiveDataObservers() {
        viewModel.classroomStudents.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
                    studentsAdapter.differ.submitList(it.data)
                }
                is Resource.Error -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
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

    private fun initialise() {
        studentsAdapter = StudentsAdapterClassRoomDetail()
        binding.recyclerViewStudentsInClass.apply {
            adapter = studentsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        classroomDetailOperations.getClassroomStudents()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}