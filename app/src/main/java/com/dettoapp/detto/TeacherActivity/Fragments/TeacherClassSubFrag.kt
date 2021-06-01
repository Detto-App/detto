package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.StudentActivity.Adapters.SubmissionAdapter
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentSubmissionViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentStudentSubmissionBinding

class TeacherClassSubFrag(private val pid: String) : BaseFragment<StudentSubmissionViewModel, FragmentStudentSubmissionBinding,
        StudentRepository>() {

    private lateinit var submissionAdapter: SubmissionAdapter

    override fun getBaseOnCreate() {
        super.getBaseOnCreate()
        viewModel.setPIDForTeacher(pid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialise()
        liveDataObservers()
    }

    private fun liveDataObservers() {
        viewModel.uploadFiles.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.pbSub.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.pbSub.visibility = View.GONE

                    submissionAdapter.differ.submitList(it.data!!)
                    if (it.data.isEmpty())
                        binding.noSub.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.pbSub.visibility = View.GONE

                }

                else -> {
                }
            }
        })
    }

    private fun initialise() {
        viewModel.getUploadedFiles()
        binding.fileChooser.visibility = View.GONE
        submissionAdapter = SubmissionAdapter(onShare = {
            onClassLinkShare(it)
        })
        binding.submissionRecyclerview.apply {
            adapter = submissionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun getViewModelClass(): Class<StudentSubmissionViewModel> {
        return StudentSubmissionViewModel::class.java
    }

    override fun getFragmentBinding(
            inflater: LayoutInflater,
            container: ViewGroup?
    ): FragmentStudentSubmissionBinding {
        return FragmentStudentSubmissionBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): StudentRepository {
        return StudentRepository(
                DatabaseDetto.getInstance(requireContext().applicationContext).classroomDAO,
                DatabaseDetto.getInstance(requireContext().applicationContext).projectDAO
        )
    }

    private fun onClassLinkShare(link: String) {
        ShareCompat.IntentBuilder.from(requireActivity())
                .setText(link)
                .setType("text/plain")
                .setChooserTitle("Game Details")
                .startChooser()
    }
}