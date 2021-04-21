package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelStoreOwner
import com.dettoapp.detto.StudentActivity.Dialog.ProjectDetailsDialog
import com.dettoapp.detto.TeacherActivity.Dialog.DeadlineDialog
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.databinding.FragmentDeadlineBinding
import com.google.android.material.datepicker.MaterialDatePicker

class DeadlineFragment(private val operations: ClassroomDetailOperations) :
    BaseFragment<ClassRoomDetailViewModel,FragmentDeadlineBinding, ClassroomDetailRepository>() ,
    DeadlineDialog.DeadlineDialogListener{

    private lateinit var dDialog: DeadlineDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initilize()
    }


    private fun initilize(){
        binding.datePicker.setOnClickListener {
            dDialog=DeadlineDialog(this)
            dDialog.show(requireActivity().supportFragmentManager,"dhsa")
        }


    }


    override fun getDeadline(dateRangePicker: MaterialDatePicker<Pair<Long, Long>>,reason :String) {
        viewModel.getDeadline(operations.getClassroom(),dateRangePicker,reason)
        Log.d("12343",""+dateRangePicker.selection!!)
        dDialog.dismiss()
    }


    override fun getViewModelClass(): Class<ClassRoomDetailViewModel> {
        return ClassRoomDetailViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
        ): FragmentDeadlineBinding {
            return FragmentDeadlineBinding.inflate(inflater,container,false)
    }

    override fun getRepository(): ClassroomDetailRepository {
        return ClassroomDetailRepository()
    }
    override fun getBaseViewModelOwner(): ViewModelStoreOwner {
        return operations.getViewModelStoreOwner()
    }
}