package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.databinding.FragmentDeadlineBinding
import com.google.android.material.datepicker.MaterialDatePicker

class DeadlineFragment(private val operations: ClassroomDetailOperations) :
    BaseFragment<ClassRoomDetailViewModel,FragmentDeadlineBinding, ClassroomDetailRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initilize()
    }

    private fun initilize(){
        binding.datePicker.setOnClickListener {
            val dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText("Select dates")
                    .setSelection(
                        Pair(
                            MaterialDatePicker.thisMonthInUtcMilliseconds(),
                            MaterialDatePicker.todayInUtcMilliseconds()
                        )
                    )
                    .build()
            dateRangePicker.show(requireActivity().supportFragmentManager," ")
            dateRangePicker.addOnPositiveButtonClickListener {
                Log.d("12343",""+dateRangePicker.selection!!)
            }
        }
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
}