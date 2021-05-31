package com.dettoapp.detto.StudentActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Models.ProjectModel
import androidx.fragment.app.Fragment
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.Adapters.StatsAdapter
import com.dettoapp.detto.StudentActivity.StudentOperations
import com.dettoapp.detto.StudentActivity.ViewModels.StatsStudentViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.BaseRepository
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentStatsStudentBinding
import com.google.common.math.Stats

class StatsStudentFragment(private val studentOperations: StudentOperations? = null, private val nullProjectModel: ProjectModel? = null) :
        BaseFragment<StatsStudentViewModel, FragmentStatsStudentBinding, BaseRepository>() {

    private lateinit var projectModel: ProjectModel
    private var githublink: String = projectModel.githublink
    private lateinit var statsAdapter: StatsAdapter

    override fun getBaseOnCreate() {
        super.getBaseOnCreate()
        projectModel = studentOperations?.getProjectModel() ?: nullProjectModel!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        liveDataObservers()
    }

    private fun liveDataObservers() {
        if (githublink.isEmpty()) {
            viewModel.githubLink.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Success -> {
                        githublink = it.data!!
                        binding.githubAddLinkGroup.visibility = View.GONE
                    }
                    else -> {
                    }
                }
            })
        }

        observeWithLiveData(viewModel.allGithub, onSuccess = {
            statsAdapter.differ.submitList(it)
            baseActivity.showToast("Submitted")
        })


    }

    override fun getViewModelClass(): Class<StatsStudentViewModel> {
        return StatsStudentViewModel::class.java
    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentStatsStudentBinding {
        return FragmentStatsStudentBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): BaseRepository {
        return BaseRepository()
    }

    private fun initialise() {
        if (githublink.isEmpty())
            binding.githubAddLinkGroup.visibility = View.VISIBLE
        else {
            binding.githubAddLinkGroup.visibility = View.GONE
            viewModel.collectData()
        }

        statsAdapter = StatsAdapter()

        binding.statsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = statsAdapter
        }

        binding.githubAddButton.setOnClickListener {
            addGitHubLink("" + binding.githubLink.editText?.text.toString().trim())
        }
    }

    private fun addGitHubLink(githubLinkLocal: String) {
        projectModel.githublink = githubLinkLocal
        viewModel.addGithubLink(githubLinkLocal,projectModel)
    }
}