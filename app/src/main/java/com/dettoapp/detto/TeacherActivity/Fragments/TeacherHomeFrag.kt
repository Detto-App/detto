package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.TeacherActivity.Dialog.GroupInfoDialog
import com.dettoapp.detto.TeacherActivity.ViewModels.TeacherHomeFragViewModel
import com.dettoapp.detto.databinding.FragmentTeacherHomeBinding

class TeacherHomeFrag : Fragment(),GroupInfoDialog.GroupInfoDialogOnClickListener {
    private lateinit var viewModel: TeacherHomeFragViewModel
    private var _binding: FragmentTeacherHomeBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(TeacherHomeFragViewModel::class.java)
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
        binding.btnfab.setOnClickListener{
            val groupInfoDialog=GroupInfoDialog(requireContext(),this)
            groupInfoDialog.show()

                }
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