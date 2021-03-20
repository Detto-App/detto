package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.LoginSignUpActivity.LoginSignUpRepository
import com.dettoapp.detto.LoginSignUpActivity.ViewModels.LoginSignUpActivityViewModelFactory
import com.dettoapp.detto.TeacherActivity.Dialog.GroupInfoDialog
import com.dettoapp.detto.TeacherActivity.TeacherRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.TeacherHomeFragFactory
import com.dettoapp.detto.TeacherActivity.ViewModels.TeacherHomeFragViewModel
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentTeacherHomeBinding

class TeacherHomeFrag : Fragment(),GroupInfoDialog.GroupInfoDialogOnClickListener {
    private lateinit var viewModel: TeacherHomeFragViewModel
    private var _binding: FragmentTeacherHomeBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = TeacherHomeFragFactory(TeacherRepository(),requireContext().applicationContext)
        viewModel = ViewModelProvider(requireActivity(),factory).get(TeacherHomeFragViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTeacherHomeBinding.inflate(inflater, container, false)
        return binding!!.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()

    }
    fun initialise(){
        binding.btnfab.setOnClickListener{
            val groupInfoDialog=GroupInfoDialog(requireContext(),this)
            groupInfoDialog.show()

        }
    }
    fun liveDataObservers(){
        viewModel.teacher.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success ->{
                    (requireActivity() as BaseActivity).hideProgressBar()
                    (requireActivity() as BaseActivity).showToast(it.data!!)
                }
                is Resource.Error -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
                    (requireActivity() as BaseActivity).showErrorSnackMessage(it.message!!)
                }
                is Resource.Loading ->{
                    (requireActivity() as BaseActivity).showProgressDialog("loading...")
                    (requireActivity() as BaseActivity).closeKeyBoard(view)
                }
                else -> {

                }
            }
        })
    }

    override fun onClassCreated(classroomname:String,sem:String,sec:String) {
        viewModel.classRoomData(classroomname,sem,sec)
    }




//    fun initialise(){
//        binding.GroupInfo.year.apply {
//            adapter = ArrayAdapter(
//                    requireContext(),
//                    android.R.layout.simple_spinner_item, roles
//            ).apply {
//                setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
//            }
//        }
//    }


}