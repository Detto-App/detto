package com.dettoapp.detto.TeacherActivity.Fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Adapters.ClassroomAdapter
import com.dettoapp.detto.TeacherActivity.DataBaseOperations
import com.dettoapp.detto.TeacherActivity.Dialog.GroupInfoDialog
import com.dettoapp.detto.TeacherActivity.Repositories.TeacherRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.TeacherHomeFragViewModelFactory
import com.dettoapp.detto.TeacherActivity.ViewModels.TeacherHomeFragViewModel
import com.dettoapp.detto.Db.ClassroomDatabase
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentTeacherHomeBinding

class TeacherHomeFrag : Fragment(), GroupInfoDialog.GroupInfoDialogOnClickListener,ClassroomAdapter.ClassRoomAdapterClickListener,DataBaseOperations {
    private lateinit var viewModel: TeacherHomeFragViewModel
    private var _binding: FragmentTeacherHomeBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var classroomAdapter: ClassroomAdapter
    private lateinit var groupInfoDialog :Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = TeacherHomeFragViewModelFactory(TeacherRepository(ClassroomDatabase.getInstance(requireContext()).classroomDAO),requireContext().applicationContext)
        viewModel = ViewModelProvider(requireActivity(),factory).get(TeacherHomeFragViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTeacherHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

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
        classroomAdapter = ClassroomAdapter(getTeacherName(),this)
        binding.teacherRecyclerView.apply {
            adapter = classroomAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun liveDataObservers() {
        viewModel.classRoomCreation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
                    groupInfoDialog.dismiss()
                    (requireActivity() as BaseActivity).showToast(it.data!!)
                }
                is Resource.Error -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
//                    (requireActivity() as BaseActivity).showErrorSnackMessage(it.message!!,requireContext().applicationContext)
                        Toast.makeText(requireContext().applicationContext,"Please Select All Fields",Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    (requireActivity() as BaseActivity).showProgressDialog(Constants.MESSAGE_LOADING)
                    (requireActivity() as BaseActivity).closeKeyBoard(view)
                }
                else -> {

                }
            }
        })

        viewModel.classRoomDeletion.observe(viewLifecycleOwner, Observer {
            when(it)
            {
                is Resource.Success -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
                }
                is Resource.Error -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
                    (requireActivity() as BaseActivity).showErrorSnackMessage(it.message!!)
                }
                is Resource.Loading -> {
                    (requireActivity() as BaseActivity).showProgressDialog(Constants.MESSAGE_LOADING)
                }
            }
        })

        viewModel.allClassRooms.observe(viewLifecycleOwner, Observer {
            classroomAdapter.differ.submitList(it)
        })
    }

    override fun onClassCreated(classname: String, sem: String, sec: String,teamSize:String,projectType:String) {
            viewModel.classRoomData(classname, sem, sec,getTeacherName(),teamSize,projectType)
    }

    private  fun getTeacherName():String{
        return viewModel.getTeacherName()
    }




    override fun onClassRoomCLicked(classroom: Classroom) {
        Utility.navigateFragment(requireActivity().supportFragmentManager, R.id.teacherHomeContainer,ClassRoomDetailFrag(classroom,this),"detailClassRoom")
    }

    override fun onClassRoomDelete(classroom: Classroom) {
        viewModel.deleteClassroom(classroom)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
