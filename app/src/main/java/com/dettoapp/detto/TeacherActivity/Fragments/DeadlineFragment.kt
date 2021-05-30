package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.TeacherActivity.Adapters.DeadlineAdapterClassroomDetail
import com.dettoapp.detto.TeacherActivity.Dialog.DeadlineDialog
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentDeadlineBinding
import com.google.android.material.datepicker.MaterialDatePicker

class DeadlineFragment(private val operations: ClassroomDetailOperations) :
    BaseFragment<ClassRoomDetailViewModel, FragmentDeadlineBinding, ClassroomDetailRepository>(),
    DeadlineDialog.DeadlineDialogListener {

    private lateinit var deadlineAdapter: DeadlineAdapterClassroomDetail
    private lateinit var dDialog: DeadlineDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()
    }

    private fun initialise() {
        deadlineAdapter = DeadlineAdapterClassroomDetail()
        binding.deadlineRecyclerView.apply {
            adapter = deadlineAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.datePicker.setOnClickListener {
            dDialog = DeadlineDialog(this)
            dDialog.show(requireActivity().supportFragmentManager, "dhsa")
        }
        operations.getDeadlineFromServer()
    }

    fun liveDataObservers() {
        viewModel.deadline.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.prgressBarDeadline.visibility = View.GONE

//                    binding.re.isRefreshing = false
                    baseActivity.hideProgressDialog()

                    deadlineAdapter.differ.submitList(it.data)

                }
                is Resource.Error -> {
                    binding.prgressBarDeadline.visibility = View.GONE
                    binding.noDeadlines.visibility = View.VISIBLE
                    binding.noDeadlines.text = "There are no Deadlines"
                }
                is Resource.Loading -> {
                    binding.prgressBarDeadline.visibility = View.VISIBLE

                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
                }
                is Resource.Confirm -> {
                    binding.prgressBarDeadline.visibility = View.GONE
                    baseActivity.hideProgressDialog()
                }
                else -> {
                }
            }
        })
    }


    override fun getDeadline(dateRangePicker: MaterialDatePicker<Pair<Long, Long>>, reason: String) {
        viewModel.getDeadline(operations.getClassroom(), dateRangePicker, reason)
        dDialog.dismiss()
    }


    override fun getViewModelClass(): Class<ClassRoomDetailViewModel> {
        return ClassRoomDetailViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDeadlineBinding {
        return FragmentDeadlineBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): ClassroomDetailRepository {
        return ClassroomDetailRepository(DatabaseDetto.getInstance(requireContext().applicationContext).rubricsDAO)
    }

    override fun getBaseViewModelOwner(): ViewModelStoreOwner {
        return operations.getViewModelStoreOwner()
    }


}