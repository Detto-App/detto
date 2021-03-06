package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.TeacherActivity.Adapters.ProjectRubricsAdapter
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentProjectHomeBinding

class ProjectHomeFragment(
    private val projectModel: ProjectModel
) : BaseFragment<ClassRoomDetailViewModel, FragmentProjectHomeBinding, ClassroomDetailRepository>() {
    private lateinit var projectRubricsAdapter: ProjectRubricsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()
    }

    private fun initialise() {
        projectRubricsAdapter = ProjectRubricsAdapter()
        binding.projectstudents.apply {
            adapter = projectRubricsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.rubricsUpdate.setOnClickListener {
            viewModel.rubricsUpdate(projectRubricsAdapter.getStudentHashMap(), projectModel)
        }
        binding.root.setOnClickListener {

        }

        viewModel.getRubricsForProject(projectModel)
    }

    fun liveDataObservers() {
        viewModel.projectRubrics.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.hideProgressDialog()
//                    binding.re.isRefreshing = false
//                    baseActivity.showToast("done")
                    projectRubricsAdapter.differ.submitList(it.data)
                    if(projectRubricsAdapter.differ.currentList.isEmpty())
                    {
                        binding.noevaluation.visibility = View.VISIBLE
                    }
                    else
                    {
                        binding.noevaluation.visibility = View.GONE
                    }
                }
                is Resource.Error -> {
//                    baseActivity.showErrorSnackMessage(it.message!!)
                    binding.noevaluation.visibility=View.VISIBLE

                }
                is Resource.Loading -> {
                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
                }
                is Resource.Confirm -> {
                    baseActivity.hideProgressDialog()

                }
                else -> {
                }
            }
        })

        viewModel.update.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    baseActivity.showProgressDialog("Loading...")
                }
                is Resource.Confirm -> {
                    binding.noevaluation.visibility = View.GONE
                    baseActivity.hideProgressDialog()
//                    binding.re.isRefreshing = false
                    baseActivity.showToast("done")
                }
                is Resource.Success -> {
                    baseActivity.hideProgressDialog()
//                    binding.re.isRefreshing = false
                    baseActivity.showToast("done")
                    //requireActivity().onBackPressed()
                }
                is Resource.Error -> {
                    baseActivity.hideProgressDialog()
//                    baseActivity.showErrorSnackMessage(it.message!!)
                    binding.noevaluation.visibility=View.VISIBLE

                }
                else -> {
                }
    }})
        }



    override fun getViewModelClass(): Class<ClassRoomDetailViewModel> {
        return ClassRoomDetailViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProjectHomeBinding {
        return FragmentProjectHomeBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): ClassroomDetailRepository {
        return ClassroomDetailRepository(DatabaseDetto.getInstance(requireContext().applicationContext).rubricsDAO)
    }


}