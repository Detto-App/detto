package com.dettoapp.detto.StudentActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.ClassroomDatabase
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.Adapters.StudentClassroomAdapter
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentHomeFragFactory
import com.dettoapp.detto.StudentActivity.ViewModels.StudentHomeFragViewModel
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentStudentHomeBinding


class StudentHomeFrag : Fragment() , StudentClassroomAdapter.AdapterAndFrag {
    private lateinit var viewModel: StudentHomeFragViewModel
    private var _binding:FragmentStudentHomeBinding?=null
    private val binding
        get() = _binding!!
    private lateinit var studentClassroomAdapter: StudentClassroomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = StudentHomeFragFactory(StudentRepository(ClassroomDatabase.getInstance(requireContext()).classroomDAO),requireContext().applicationContext)
        viewModel = ViewModelProvider(requireActivity(),factory).get(StudentHomeFragViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStudentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        liveDataObservers()
    }
    private fun initialise(){
        studentClassroomAdapter = StudentClassroomAdapter(this)
        binding.studentRecyclerView.apply {
            adapter = studentClassroomAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun liveDataObservers(){
        viewModel.allClassRooms.observe(viewLifecycleOwner, Observer {
            studentClassroomAdapter.differ.submitList(it)
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun communicate() {
        Utility.navigateFragment(requireActivity().supportFragmentManager, R.id.StudentFragContainer,
            StudentClassDetailsFrag(),"abcd",true)

    }


}