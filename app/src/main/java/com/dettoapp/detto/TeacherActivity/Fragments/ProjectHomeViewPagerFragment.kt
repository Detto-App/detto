package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.TeacherActivity.Adapters.ClassRoomDetailFragViewPagerAdapter
import com.dettoapp.detto.TeacherActivity.Adapters.ProjectAdapterClassroomDetail
import com.dettoapp.detto.TeacherActivity.Adapters.ProjectHomeFragViewPagerAdapter
import com.dettoapp.detto.TeacherActivity.Adapters.ProjectRubricsShowAdapter
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.databinding.FragmentClassroomProjectsBinding
import com.dettoapp.detto.databinding.FragmentProjectHomeViewpagerBinding
import com.google.android.material.tabs.TabLayoutMediator

class ProjectHomeViewPagerFragment(projectModel: ProjectModel): BaseFragment<ClassRoomDetailViewModel, FragmentProjectHomeViewpagerBinding, ClassroomDetailRepository>() {

    private val projectModel=projectModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
    }
    private fun initialise() {
        liveDataObservers()
    }
    fun liveDataObservers() {
        val viewPagerAdapter = ProjectHomeFragViewPagerAdapter(requireActivity(),projectModel)
        binding.projecthomeviewpager.adapter = viewPagerAdapter

        TabLayoutMediator(
                binding.projecthometablayout,
                binding.projecthomeviewpager
        ) { tab, position ->
            tab.text = Constants.projectHomeFragTabNames[position]
            binding.projecthomeviewpager.setCurrentItem(tab.position, true)
        }.attach()

    }

        override fun getViewModelClass(): Class<ClassRoomDetailViewModel> {
        return ClassRoomDetailViewModel::class.java
    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentProjectHomeViewpagerBinding {
        return FragmentProjectHomeViewpagerBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): ClassroomDetailRepository {
        return ClassroomDetailRepository(DatabaseDetto.getInstance(requireContext().applicationContext).rubricsDAO)
    }
}