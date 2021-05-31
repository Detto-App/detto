package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Chat.ChatFragment
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Adapters.ClassDetailOptionsAdapter
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
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import kotlin.collections.ArrayList

class ClassRoomDetailFrag(
    private val classroom: Classroom,
    private val dataBaseOperations: DataBaseOperations
) : BaseFragment<ClassRoomDetailViewModel, FragmentClassRoomDetailBinding, ClassroomDetailRepository>(),
    ClassRoomDetailModal.ClassRoomDetailModalClickListener, ClassroomDetailOperations, ClassDetailOptionsAdapter.ClassDetailOptionsInterface {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()
    }

    private fun initialise() {

        FirebaseMessaging.getInstance().subscribeToTopic(classroom.classroomuid)

        binding.getReport.setOnClickListener {
            viewModel.getReport()
        }

        binding.classRoomDetailName.text = classroom.classroomname.capitalize(Locale.ROOT)
        binding.classRoomDetailTeacherName.text =
            "By -" + classroom.teacher.name.capitalize(Locale.ROOT)
        if (classroom.settingsModel.groupType == Constants.AUTO) {
            binding.formTeams.visibility = View.VISIBLE
        }

        binding.classRoomDetailMenu.setOnClickListener {
            showBottomDialog()
        }

        binding.root.setOnClickListener {

        }

        binding.teacherChatButton.setOnClickListener {
            Utility.navigateFragment(
                requireActivity().supportFragmentManager,
                R.id.teacherHomeContainer,
                ChatFragment(classroom, Utility.TEACHER.name + " - Teacher", Utility.TEACHER.uid,""),
                "chat"
            )

        }

        val list = arrayListOf("Students","Deadlines","Rubrics")

        binding.classDetailOptionsRV.apply {
            layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
            adapter = ClassDetailOptionsAdapter(list,this@ClassRoomDetailFrag)
        }

        binding.sendNotification.setOnClickListener {
            viewModel.sendNotification(classroom)
        }
        binding.formTeams.setOnClickListener {

            viewModel.formTeams(classroom)
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

    fun liveDataObservers() {
        viewModel.autoProject.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.hideProgressDialog()
//                    binding.re.isRefreshing = false
                    baseActivity.showToast("done")
                }
                is Resource.Error -> {
                    baseActivity.showErrorSnackMessage(it.message!!)
                }
                is Resource.Loading -> {
//                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
                }
                is Resource.Confirm -> {
                    baseActivity.showToast("done")
                    baseActivity.hideProgressDialog()
                }
                else -> {
                }
            }
        })
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

    override fun getClassroom(): String {
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
        return ClassroomDetailRepository(DatabaseDetto.getInstance(requireContext().applicationContext).rubricsDAO)
    }

    override fun onOptionClicked(type: String) {
        when(type)
        {
            "Students" -> Utility.navigateFragment(requireActivity().supportFragmentManager,R.id.teacherHomeContainer,StudentsInClassFragment(this),"students")
            "Deadlines" -> Utility.navigateFragment(requireActivity().supportFragmentManager,R.id.teacherHomeContainer,DeadlineFragment(this),"Deadlines")
            "Rubrics" -> Utility.navigateFragment(requireActivity().supportFragmentManager,R.id.teacherHomeContainer,RubricsFragment(this),"rubrics")
        }
    }


}