package com.dettoapp.detto.StudentActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.ViewModels.StudentHomeFragViewModel
import com.dettoapp.detto.TeacherActivity.TeacherRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.TeacherHomeFragFactory
import com.dettoapp.detto.TeacherActivity.ViewModels.TeacherHomeFragViewModel
import com.dettoapp.detto.databinding.FragmentLoginBinding
import com.dettoapp.detto.databinding.FragmentStudentHomeBinding
import com.dettoapp.detto.databinding.FragmentTeacherHomeBinding


class StudentHomeFrag : Fragment() {
    private lateinit var viewModel: StudentHomeFragViewModel
    private var _binding:FragmentStudentHomeBinding?=null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = TeacherHomeFragFactory(TeacherRepository(),requireContext().applicationContext)
        viewModel = ViewModelProvider(requireActivity(),factory).get(StudentHomeFragViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStudentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        liveDataObservers()
    }
    private fun initialise(){

    }

    private fun liveDataObservers(){

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}