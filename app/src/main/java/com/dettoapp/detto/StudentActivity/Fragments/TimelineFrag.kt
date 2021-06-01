package com.dettoapp.detto.StudentActivity.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.StudentActivity.Adapters.TimelineAdapter
import com.dettoapp.detto.StudentActivity.StudentOperations
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.TimelineViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentTimelineBinding


class TimelineFrag(private val cid: String, private val studentOperations: StudentOperations) :
    BaseFragment<TimelineViewModel, FragmentTimelineBinding, StudentRepository>() {

    private lateinit var timelineAdapter: TimelineAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()
    }

    override fun getBaseOnCreate() {
        viewModel.getProjectFromSharedPrefForTodo(cid)
    }

    fun liveDataObservers() {
        viewModel.timeline.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
//                    Log.d("123","5"+it.data.toString())
                    timelineAdapter.differ.submitList(it.data)
                }
                is Resource.Error -> {
//                    baseActivity.showErrorSnackMessage(it.message!!)
                    binding.progressBar.visibility=View.GONE
                    binding.notimeline.visibility=View.VISIBLE
                }
                else -> {
                }
            }
        })
    }

    private fun initialise() {
        viewModel.getTimeline()
        Log.d("123", "2")
        timelineAdapter = TimelineAdapter()
        binding.timelinerecyclerview.apply {
            adapter = timelineAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    override fun getViewModelClass(): Class<TimelineViewModel> {
        return TimelineViewModel::class.java
    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTimelineBinding {
        return FragmentTimelineBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): StudentRepository {
        return StudentRepository(
            DatabaseDetto.getInstance(requireContext().applicationContext).classroomDAO,
            DatabaseDetto.getInstance(requireContext().applicationContext).projectDAO
        )
    }


}