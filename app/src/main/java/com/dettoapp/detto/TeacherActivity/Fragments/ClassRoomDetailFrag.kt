package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.Db.ClassroomDatabase
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.TeacherActivity.Adapters.ClassRoomDetailFragViewPagerAdapter
import com.dettoapp.detto.TeacherActivity.DataBaseOperations
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.TeacherActivity.Repositories.TeacherRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassroomDetailViewModelFactory
import com.dettoapp.detto.TeacherActivity.ViewModels.TeacherHomeFragViewModel
import com.dettoapp.detto.TeacherActivity.ViewModels.TeacherHomeFragViewModelFactory
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.databinding.FragmentClassRoomDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class ClassRoomDetailFrag(
    private val classroom: Classroom,
    private val dataBaseOperations: DataBaseOperations
) : Fragment(), ClassRoomDetailModal.ClassRoomDetailModalClickListener, ClassroomDetailOperations {

    private lateinit var viewModel: ClassRoomDetailViewModel
    private var _binding: FragmentClassRoomDetailBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = ClassroomDetailViewModelFactory(
            ClassroomDetailRepository(),
            requireContext().applicationContext
        )
        viewModel = ViewModelProvider(this, factory).get(ClassRoomDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentClassRoomDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise(view)
    }

    private fun initialise(view: View) {
        binding.classRoomDetailName.text = classroom.classroomname
        binding.classRoomDetailTeacherName.text = "Demo"

        binding.classRoomDetailMenu.setOnClickListener {
            showBottomDialog()
        }

        view.setOnClickListener {

        }

        val viewPagerAdapter = ClassRoomDetailFragViewPagerAdapter(requireActivity(), this)
        binding.viewPagerClassroomDetailFrag.adapter = viewPagerAdapter

        TabLayoutMediator(
            binding.tabLayoutClassroomDetailFrag,
            binding.viewPagerClassroomDetailFrag
        ) { tab, position ->
            tab.text = Constants.classDetailFragTabNames[position]
            binding.viewPagerClassroomDetailFrag.setCurrentItem(tab.position, true)
        }.attach()
    }

    private fun showBottomDialog() {
        val bottomSheet = ClassRoomDetailModal(this)
        bottomSheet.show(requireActivity().supportFragmentManager, "classroomModal")
    }

    override fun onDeletePressed() {
        dataBaseOperations.onClassRoomDelete(classroom)
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun getClassroomStudents() {
        viewModel.getClassStudents(classroom)
    }
}