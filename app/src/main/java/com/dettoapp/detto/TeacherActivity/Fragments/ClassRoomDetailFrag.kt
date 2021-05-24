package com.dettoapp.detto.TeacherActivity.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelStoreOwner
import com.dettoapp.detto.Chat.ChatFragment
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Adapters.ClassRoomDetailFragViewPagerAdapter
import com.dettoapp.detto.TeacherActivity.DataBaseOperations
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentClassRoomDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

class ClassRoomDetailFrag(
    private val classroom: Classroom,
    private val dataBaseOperations: DataBaseOperations
) : BaseFragment<ClassRoomDetailViewModel, FragmentClassRoomDetailBinding, ClassroomDetailRepository>(),
    ClassRoomDetailModal.ClassRoomDetailModalClickListener, ClassroomDetailOperations {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise(view)
    }

    private fun initialise(view: View) {

        FirebaseMessaging.getInstance().subscribeToTopic(classroom.classroomuid)


        binding.classRoomDetailName.text = classroom.classroomname.capitalize(Locale.ROOT)
        binding.classRoomDetailTeacherName.text =
            "By -" + classroom.teacher.name.capitalize(Locale.ROOT)

        binding.classRoomDetailMenu.setOnClickListener {
            showBottomDialog()
        }

        view.setOnClickListener {

        }

        binding.teacherChatButton.setOnClickListener {
            Utility.navigateFragment(requireActivity().supportFragmentManager, R.id.teacherHomeContainer, ChatFragment(classroom, Utility.TEACHER.name + " - Teacher" , Utility.TEACHER.uid), "chat")

        }

        binding.sendNotification.setOnClickListener {
            viewModel.sendNotification(classroom)
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


    override fun getProjects() {
        viewModel.getProjects(classroom.classroomuid)
    }

    override fun getClassroom():String{
        return classroom.classroomuid
    }

    override fun getDeadlineFromServer() {
        viewModel.getDeadlineFromServer(classroom.classroomuid)
    }


    override fun getViewModel(): ClassRoomDetailViewModel {
        return viewModel
    }

    override fun getViewModelStoreOwner(): ViewModelStoreOwner {
        return this
    }

    override fun getViewModelClass(): Class<ClassRoomDetailViewModel> {
        return ClassRoomDetailViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentClassRoomDetailBinding {
        return FragmentClassRoomDetailBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): ClassroomDetailRepository {
        return ClassroomDetailRepository()
    }

}