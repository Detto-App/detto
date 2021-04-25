package com.dettoapp.detto.StudentActivity.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import com.dettoapp.detto.Chat.ChatFragment
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.Adapters.StudentHomeViewPagerAdapter
import com.dettoapp.detto.StudentActivity.Dialog.ProjectDetailsDialog
import com.dettoapp.detto.StudentActivity.Dialog.ProjectEditDialog
import com.dettoapp.detto.StudentActivity.StudentOperations
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentClassDetailViewModel
import com.dettoapp.detto.TeacherActivity.Adapters.ClassRoomDetailFragViewPagerAdapter
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentStudentClassDetailsBinding
import com.google.android.material.tabs.TabLayoutMediator


class StudentClassDetailsFrag(private val classroom: Classroom) : BaseFragment<StudentClassDetailViewModel, FragmentStudentClassDetailsBinding, StudentRepository>(),
        ProjectDetailsDialog.ProjectDialogClickListener, ProjectEditDialog.ProjectEditDialogClickListner ,
        StudentOperations {


    private lateinit var projectModel: ProjectModel
    private lateinit var pDialog: ProjectDetailsDialog
    private lateinit var projectEditDialog: ProjectEditDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise(view)
        liveDataObservers()
    }

    private fun initialise(view: View) {
        viewModel.getProject(classroom.classroomuid)
        binding.stuClassDetailsbutton.setOnClickListener {
            pDialog = ProjectDetailsDialog(this)
            pDialog.show(requireActivity().supportFragmentManager, "pCreate")
        }
        view.setOnClickListener { }

        if (viewModel.getProjectFromSharedPref(classroom) == Constants.PROJECT_NOT_CREATED)
            binding.noProjectContent.visibility = View.VISIBLE
        else
            binding.yesProjectContent.visibility = View.VISIBLE

        binding.checkStatus.setOnClickListener {
            viewModel.checkProjectStatus(projectModel.pid)
        }

        binding.chat.setOnClickListener {
            Utility.navigateFragment(requireActivity().supportFragmentManager, R.id.StudentFragContainer, ChatFragment(classroom,Utility.STUDENT.name + " - " + Utility.STUDENT.susn,Utility.STUDENT.uid), "chat")
        }

        binding.edit.setOnClickListener {
            projectEditDialog = ProjectEditDialog(this)
            projectEditDialog.show(requireActivity().supportFragmentManager, "pEdit")
        }

        val viewPagerAdapter = StudentHomeViewPagerAdapter(requireActivity(),classroom,this)
        binding.studentinclassviewpager.adapter = viewPagerAdapter

        TabLayoutMediator(
            binding.tabLayoutStudentClassDetail,
            binding.studentinclassviewpager
        ) { tab, position ->
            tab.text = Constants.studentClassDetailFragTabNames[position]
            binding.studentinclassviewpager.setCurrentItem(tab.position, true)
        }.attach()

    }

    override fun onProjectCreate(title: String, description: String, usnMap: HashMap<Int, String>, arrayList: ArrayList<String>) {
        viewModel.storeProject(title, description, usnMap, classroom, arrayList)
    }

    override fun onProjectEdit(title: String, description: String) {
        viewModel.storeEditedProject(classroom.classroomuid, title, description)
        projectEditDialog.dismiss()
    }


    override fun getClassroom(): Classroom {
        return classroom
    }

    private fun liveDataObservers() {

        observeWithLiveData(viewModel.stuProjectCreation, onSuccess = {
            baseActivity.hideProgressDialog()
            pDialog.dismiss()
            setUpProjectDisplayContent(it)
            viewModel.stuProjectCreation.removeObservers(viewLifecycleOwner)
        }, onError = {
            baseActivity.hideProgressDialog()
            baseActivity.showErrorSnackMessage(it, pDialog.getViewDialog())
        }, onLoading = {
            baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
        })

        viewModel.project.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    setUpProjectDisplayContent(it.data!!)
                    if (it.data.status == Constants.PROJECT_ACCEPTED) {
                        binding.statusDisplay1.setBackgroundColor(Color.GREEN)
                        binding.statusDisplay1.setText(Constants.PROJECT_ACCEPTED)
                        binding.checkStatus.visibility = View.GONE
                        binding.edit.visibility = View.GONE

                    } else if (it.data.status == Constants.PROJECT_REJECTED) {
                        binding.statusDisplay1.setBackgroundColor(Color.RED)
                        binding.statusDisplay1.setText(Constants.PROJECT_REJECTED)
                        binding.checkStatus.visibility = View.GONE
                        binding.edit.visibility = View.VISIBLE
                    } else if (it.data.status == Constants.PROJECT_PENDING) {
                        binding.statusDisplay1.setBackgroundColor(Color.YELLOW)
                        binding.statusDisplay1.setText(Constants.PROJECT_PENDING)
                        binding.checkStatus.visibility = View.VISIBLE
                        binding.edit.visibility = View.GONE
                    }
                }
                is Resource.Error -> showHideProjectContent()
                else -> {
                }
            }
        })
    }

    private fun setUpProjectDisplayContent(projectModelLocal: ProjectModel) {
        showHideProjectContent(isShowingProjectContent = true)
        projectModel = projectModelLocal
        setUpProjectDetails()
    }

    private fun showHideProjectContent(isShowingProjectContent: Boolean = false) {
        if (isShowingProjectContent) {
            binding.noProjectContent.visibility = View.GONE
            binding.yesProjectContent.visibility = View.VISIBLE
        } else {
            binding.noProjectContent.visibility = View.VISIBLE
            binding.yesProjectContent.visibility = View.GONE
        }
    }

    private fun setUpProjectDetails() {
        binding.pNameStudentClass.text = projectModel.title
        binding.pDescStudentClass.text = projectModel.desc

        val shareLink = "https://detto.uk.to/pid/" + projectModel.pid

        binding.shareProjectLink.setOnClickListener {
            ShareCompat.IntentBuilder.from(requireActivity())
                    .setText(shareLink).setType("text/plain")
                    .setChooserTitle("Game Details")
                    .startChooser()
        }
    }

    override fun getViewModelClass(): Class<StudentClassDetailViewModel> = StudentClassDetailViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentStudentClassDetailsBinding {
        return FragmentStudentClassDetailsBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): StudentRepository = StudentRepository(
            DatabaseDetto.getInstance(requireContext().applicationContext).classroomDAO,
            DatabaseDetto.getInstance(requireContext().applicationContext).projectDAO
    )

    override fun getViewModelOwner():ViewModelStoreOwner {
        return this
    }
}