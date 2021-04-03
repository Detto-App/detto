package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Adapters.ClassroomAdapter
import com.dettoapp.detto.TeacherActivity.DataBaseOperations
import com.dettoapp.detto.TeacherActivity.Dialog.GroupInfoDialog
import com.dettoapp.detto.TeacherActivity.Repositories.TeacherRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.TeacherHomeFragViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentTeacherHomeBinding

class TeacherHomeFrag : BaseFragment<TeacherHomeFragViewModel, FragmentTeacherHomeBinding, TeacherRepository>(), GroupInfoDialog.GroupInfoDialogOnClickListener, ClassroomAdapter.ClassRoomAdapterClickListener, DataBaseOperations {

    private lateinit var classroomAdapter: ClassroomAdapter
    private lateinit var groupInfoDialog: GroupInfoDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()
    }

    private fun initialise() {
        binding.btnfab.setOnClickListener {
            groupInfoDialog = GroupInfoDialog(requireContext(), this)
            groupInfoDialog.show()
        }
        classroomAdapter = ClassroomAdapter(viewModel.getTeacherName(), this)
        binding.teacherRecyclerView.apply {
            adapter = classroomAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun liveDataObservers() {
        viewModel.classRoomCreation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.hideProgressDialog()
                    groupInfoDialog.dismiss()
                    baseActivity.showToast(it.data!!)
                }
                is Resource.Error -> {

                    baseActivity.hideProgressDialog()
                    baseActivity.showErrorSnackMessage(it.message!!, groupInfoDialog.getViewDialog())
                    Toast.makeText(requireContext().applicationContext, "Please Select All Fields", Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
                    baseActivity.closeKeyBoard(view)
                }
                else -> {

                }
            }
        })

        viewModel.classRoomDeletion.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.hideProgressDialog()
                }
                is Resource.Error -> {
                    baseActivity.hideProgressDialog()
                    baseActivity.showErrorSnackMessage(it.message!!)
                }
                is Resource.Loading -> {
                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
                }
            }
        })

        viewModel.allClassRooms.observe(viewLifecycleOwner, Observer {
            classroomAdapter.differ.submitList(it)
        })
    }

    override fun onClassCreated(classname: String, sem: String, sec: String, teamSize: String, projectType: String) {
        viewModel.classRoomData(classname, sem, sec, teamSize, projectType)
    }

    override fun onClassRoomCLicked(classroom: Classroom) {
        Utility.navigateFragment(requireActivity().supportFragmentManager, R.id.teacherHomeContainer, ClassRoomDetailFrag(classroom, this), "detailClassRoom")
    }

    override fun onClassLinkShare(link: String) {
        ShareCompat.IntentBuilder.from(requireActivity())
                .setText(link)
                .setType("text/plain")
                .setChooserTitle("Game Details")
                .startChooser()
    }

    override fun onClassRoomDelete(classroom: Classroom) {
        viewModel.deleteClassroom(classroom)
    }


    override fun getViewModelClass(): Class<TeacherHomeFragViewModel> = TeacherHomeFragViewModel::class.java

    override fun getFragmentBinding(
            inflater: LayoutInflater,
            container: ViewGroup?
    ): FragmentTeacherHomeBinding = FragmentTeacherHomeBinding.inflate(inflater, container, false)

    override fun getRepository(): TeacherRepository = TeacherRepository(DatabaseDetto.getInstance(requireContext().applicationContext).classroomDAO)
}
