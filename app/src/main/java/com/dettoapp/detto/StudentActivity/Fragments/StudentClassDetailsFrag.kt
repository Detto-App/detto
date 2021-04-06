package com.dettoapp.detto.StudentActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.StudentActivity.Dialog.ProjectDetailsDialog
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentClassDetailViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentStudentClassDetailsBinding


class StudentClassDetailsFrag(private val classroom: Classroom) :
    BaseFragment<StudentClassDetailViewModel, FragmentStudentClassDetailsBinding, StudentRepository>(),
    ProjectDetailsDialog.ProjectDialogClickListener {

    private lateinit var projectModel: ProjectModel
    private lateinit var pDialog: ProjectDetailsDialog

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
//
//        if (viewModel.getProjectFromSharedPref(classroom) == Constants.PROJECT_NOT_CREATED)
//            binding.noProjectContent.visibility = View.VISIBLE
//        else
//            binding.yesProjectContent.visibility = View.VISIBLE
    }

    override fun onProjectCreate(title: String, description: String, usnMap: HashMap<Int, String>, arrayList: ArrayList<String>) {
        viewModel.storeProject(title, description, usnMap, classroom,arrayList)
    }


    override fun getClassroom(): Classroom {
        return classroom
    }

    private fun liveDataObservers() {
        viewModel.stuProjectCreation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.hideProgressDialog()
                    pDialog.dismiss()
                    setUpProjectDisplayContent(it.data!!)
                    viewModel.stuProjectCreation.removeObservers(viewLifecycleOwner)
                }
                is Resource.Error -> {
                    baseActivity.hideProgressDialog()
                    baseActivity.showErrorSnackMessage(it.message!!, pDialog.getViewDialog())
                }
                is Resource.Loading -> {
                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
                }
                else -> {
                }
            }
        })

        viewModel.project.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    setUpProjectDisplayContent(it.data!!)
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
}