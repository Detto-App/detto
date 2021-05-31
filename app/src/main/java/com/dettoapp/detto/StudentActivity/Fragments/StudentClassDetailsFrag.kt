package com.dettoapp.detto.StudentActivity.Fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.dettoapp.detto.TeacherActivity.Adapters.ClassDetailOptionsAdapter
import com.dettoapp.detto.TeacherActivity.Fragments.DeadlineFragment
import com.dettoapp.detto.TeacherActivity.Fragments.RubricsFragment
import com.dettoapp.detto.TeacherActivity.Fragments.StudentsInClassFragment
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentStudentClassDetailsBinding
import com.google.android.material.tabs.TabLayoutMediator


class StudentClassDetailsFrag(private val classroom: Classroom) :
        BaseFragment<StudentClassDetailViewModel, FragmentStudentClassDetailsBinding, StudentRepository>(),
        ProjectDetailsDialog.ProjectDialogClickListener,
        ProjectEditDialog.ProjectEditDialogClickListner,
        StudentOperations, ClassDetailOptionsAdapter.ClassDetailOptionsInterface {
    private lateinit var projectModel: ProjectModel
    private lateinit var pDialog: ProjectDetailsDialog
    private lateinit var projectEditDialog: ProjectEditDialog

    override fun getBaseOnCreate() {
        viewModel.getProject(classroom.classroomuid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise(view)
        liveDataObservers()
    }

    private fun initialise(view: View) {

        binding.stuClassDetailsbutton.setOnClickListener {
            pDialog = ProjectDetailsDialog(this)
            pDialog.show(requireActivity().supportFragmentManager, "pCreate")
        }
        view.setOnClickListener { }

        if (viewModel.getProjectFromSharedPref(classroom.classroomuid) == Constants.PROJECT_NOT_CREATED)
            binding.noProjectContent.visibility = View.VISIBLE
        else
            binding.yesProjectContent.visibility = View.VISIBLE

        binding.checkStatus.setOnClickListener {
            viewModel.checkProjectStatus(projectModel.pid)
        }

        binding.chat.setOnClickListener {
            Utility.navigateFragment(
                    requireActivity().supportFragmentManager,
                    R.id.StudentFragContainer,
                    ChatFragment(
                            classroom,
                            Utility.STUDENT.name + " - " + Utility.STUDENT.susn,
                            Utility.STUDENT.uid,
                            viewModel.tempProject.pid
                    ),
                    "chat"
            )
        }

        binding.edit.setOnClickListener {
            projectEditDialog = ProjectEditDialog(this)
            projectEditDialog.show(requireActivity().supportFragmentManager, "pEdit")
        }

        val viewPagerAdapter = StudentHomeViewPagerAdapter(
                requireActivity(), classroom, this
        )

        binding.studentinclassviewpager.adapter = viewPagerAdapter

        TabLayoutMediator(
                binding.tabLayoutStudentClassDetail,
                binding.studentinclassviewpager
        ) { tab, position ->
            tab.text = Constants.studentClassDetailFragTabNames[position]
            binding.studentinclassviewpager.setCurrentItem(tab.position, true)
        }.attach()


        val list = arrayListOf("Deadlines", "Todo", "Stats", "Timeline")

        binding.studentOptions.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ClassDetailOptionsAdapter(list, this@StudentClassDetailsFrag)
        }

    }

    override fun onProjectCreate(
            title: String,
            description: String,
            usnMap: HashMap<Int, String>,
            arrayList: ArrayList<String>
    ) {

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
                        binding.statusDisplay1.text = Constants.PROJECT_ACCEPTED
                        binding.checkStatus.visibility = View.GONE
                        binding.edit.visibility = View.GONE


                    } else if (it.data.status == Constants.PROJECT_REJECTED) {
                        binding.statusDisplay1.setBackgroundColor(Color.RED)
                        binding.statusDisplay1.text = Constants.PROJECT_REJECTED
                        binding.checkStatus.visibility = View.GONE
                        binding.edit.visibility = View.VISIBLE
                    } else if (it.data.status == Constants.PROJECT_PENDING) {
                        binding.statusDisplay1.setBackgroundColor(Color.YELLOW)
                        binding.statusDisplay1.text = Constants.PROJECT_PENDING
                        binding.checkStatus.visibility = View.VISIBLE
                        binding.edit.visibility = View.GONE

                    }
                }
                is Resource.Error -> showHideProjectContent(classroom.settingsModel.groupType)
                else -> {
                }
            }
        })
    }

    private fun setUpProjectDisplayContent(projectModelLocal: ProjectModel) {
        showHideProjectContent(classroom.settingsModel.groupType, isShowingProjectContent = true)
        projectModel = projectModelLocal
        setUpProjectDetails()
    }

    @SuppressLint("SetTextI18n")
    private fun showHideProjectContent(projectType: String, isShowingProjectContent: Boolean = false) {
        if (isShowingProjectContent) {
            binding.noProjectContent.visibility = View.GONE
            binding.yesProjectContent.visibility = View.VISIBLE
            binding.stuClassDetailsbutton.visibility = View.GONE

        } else if (projectType == Constants.MANUAL) {
            binding.noProjectContent.visibility = View.VISIBLE
            binding.yesProjectContent.visibility = View.GONE
            binding.stuClassDetailsbutton.visibility = View.VISIBLE

        } else {
            binding.noProjectContent.visibility = View.VISIBLE
            binding.yesProjectContent.visibility = View.GONE
            binding.stuClassDetailsbutton.visibility = View.GONE
            binding.details.text = "Please Wait For Teacher To Allot You To A Project Group"


        }
    }

    private fun setUpProjectDetails() {
        binding.pNameStudentClass.text = projectModel.desc
        binding.pDescStudentClass.text = projectModel.title

        val shareLink = "https://detto.uk.to/pid/" + projectModel.pid

        binding.shareProjectLink.setOnClickListener {
            ShareCompat.IntentBuilder.from(requireActivity())
                    .setText(shareLink).setType("text/plain")
                    .setChooserTitle("Game Details")
                    .startChooser()
        }
    }

    override fun getViewModelClass(): Class<StudentClassDetailViewModel> =
            StudentClassDetailViewModel::class.java

    override fun getFragmentBinding(
            inflater: LayoutInflater,
            container: ViewGroup?
    ): FragmentStudentClassDetailsBinding {

        return FragmentStudentClassDetailsBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): StudentRepository = StudentRepository(
            DatabaseDetto.getInstance(requireContext().applicationContext).classroomDAO,
            DatabaseDetto.getInstance(requireContext().applicationContext).projectDAO
    )

    override fun getViewModelOwner(): ViewModelStoreOwner {
        return this
    }

    override fun getProjectModel() = projectModel
    override fun onOptionClicked(type: String) {
        when (type) {
            "Todo" -> Utility.navigateFragment(requireActivity().supportFragmentManager, R.id.StudentFragContainer, TodoFrag(classroom.classroomuid, this), "students")
            "Deadlines" -> Utility.navigateFragment(requireActivity().supportFragmentManager, R.id.StudentFragContainer, StudentDeadlineFrag(classroom, this), "stuDead")
            "Stats" -> Utility.navigateFragment(requireActivity().supportFragmentManager, R.id.StudentFragContainer, StatsStudentFragment(this), "stats")
            "Timeline" -> Utility.navigateFragment(requireActivity().supportFragmentManager, R.id.StudentFragContainer, TimelineFrag(classroom.classroomuid, this), "timeLine")
        }
    }
}