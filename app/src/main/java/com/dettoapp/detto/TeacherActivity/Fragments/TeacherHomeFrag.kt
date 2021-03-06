package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.AccessModel
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Adapters.ClassroomAdapter
import com.dettoapp.detto.TeacherActivity.DataBaseOperations
import com.dettoapp.detto.TeacherActivity.Dialog.AddAccessDialog
import com.dettoapp.detto.TeacherActivity.Repositories.TeacherRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.TeacherHomeFragViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentTeacherHomeBinding


class TeacherHomeFrag : BaseFragment<TeacherHomeFragViewModel, FragmentTeacherHomeBinding, TeacherRepository>(),
    ClassroomCreateFragment.ClassroomCreateFragmentOnClickListener, ClassroomAdapter.ClassRoomAdapterClickListener,
    DataBaseOperations, AddAccessDialog.AddAccessDialogListener {

    private lateinit var classroomAdapter: ClassroomAdapter
    private lateinit var classroomCreateFragment: ClassroomCreateFragment
    private lateinit var addAccessDialog: AddAccessDialog
    var list=ArrayList<AccessModel>()
    var accesLevels = ArrayList<String>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()
    }

    private fun initialise() {
        if(!("Teacher" in accesLevels))
            accesLevels.add("Teacher")

        Log.d("PPW","fdhjdh")
        viewModel.getTeacherModelFromServer()
        if(list !=null)
            for(i in list)
                accesLevels.add(i.type+" "+i.sem)
        val accessAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, accesLevels)
        binding.accessmenu.setAdapter(accessAdapter)

        binding.accessmenu.setOnItemClickListener { _, _, position, value ->
            Log.d("EER",accesLevels.toString())

            val selected = accesLevels[position]
            val access = selected.subSequence(0, selected.length - 2)
            val sem = selected.last()
            changeAccess(access.toString(), sem.toString())
        }
        binding.btnfab.setOnClickListener {
            Utility.navigateFragment(
                requireActivity().supportFragmentManager,
                R.id.teacherHomeContainer,
                ClassroomCreateFragment(this),
                "ddd"
            )
        }
        classroomAdapter = ClassroomAdapter(viewModel.getTeacherName(), this)
        binding.teacherRecyclerView.apply {
            adapter = classroomAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.teacherRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 20) binding.btnfab.hide() else if (dy < 20) binding.btnfab.show()
            }
        })
        binding.addaccess.setOnClickListener {
            addAccessDialog = AddAccessDialog(this)
            addAccessDialog.show(requireActivity().supportFragmentManager, "add access dialog")
        }

    }

    private fun liveDataObservers() {
        viewModel.classRoomCreation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.hideProgressDialog()
                    //groupInfoDialog.dismiss()
                    baseActivity.showToast(it.data!!)
                }
                is Resource.Error -> {

                    baseActivity.hideProgressDialog()
                    baseActivity.showErrorSnackMessage(it.message!!, classroomCreateFragment.getViewDialog())
                    Toast.makeText(requireContext().applicationContext, "Please Select All Fields", Toast.LENGTH_LONG)
                        .show()
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
        viewModel.getTeacherModel.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.hideProgressDialog()
                    list=it.data!!.accessmodelist
                    accesLevels =ArrayList<String>()
                    accesLevels.add("Teacher")
                    if(list !=null)
                        for(i in list)
                            accesLevels.add(i.type+" "+i.sem)
                    val accessAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, accesLevels)
                    binding.accessmenu.setAdapter(accessAdapter)
//                    binding.accessmenu.setAdapter(accessAdapter)


//                    initialise()
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
        viewModel.access.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.showToast("Success")
                    viewModel.getTeacherModelFromServer()
//                    initialise()
//                    liveDataObservers()

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

        viewModel.accessChange.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.showToast("Success")
                    classroomAdapter.differ.submitList(it.data)
//                    initialise()
//                    liveDataObservers()

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

    override fun onClassCreated(
        classname: String,
        sem: String,
        sec: String,
        teamSize: String,
        projectType: String,
        groupType: String
    ) {
        viewModel.classRoomData(classname, sem, sec, teamSize, projectType, groupType)
        activity?.supportFragmentManager?.popBackStack()

    }

    override fun onClassRoomClicked(classroom: Classroom) {
        Utility.navigateFragment(
            requireActivity().supportFragmentManager,
            R.id.teacherHomeContainer,
            ClassRoomDetailFrag(classroom, this),
            "detailClassRoom"
        )
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

    override fun getRepository(): TeacherRepository =
        TeacherRepository(DatabaseDetto.getInstance(requireContext().applicationContext).classroomDAO)

    override fun addAccess(access: String, sem: String) {

        Log.d("PPW","222")
        viewModel.addAccess(access,sem)
        addAccessDialog.dismiss()
    }

    private fun changeAccess(access: String, sem: String) {

        viewModel.changeAccess(access, sem)
    }

}
