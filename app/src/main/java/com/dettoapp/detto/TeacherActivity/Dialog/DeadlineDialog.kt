package com.dettoapp.detto.TeacherActivity.Dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.core.util.Pair
import androidx.fragment.app.DialogFragment
import com.dettoapp.detto.R
import com.dettoapp.detto.databinding.FragmentDeadlineDialogBinding
import com.google.android.material.datepicker.MaterialDatePicker


class DeadlineDialog(private val deadlineDialogListener: DeadlineDialogListener) : DialogFragment() {

    private val binding:FragmentDeadlineDialogBinding  by viewBinding()

    interface DeadlineDialogListener{
        fun getDeadline( dateRangePicker: MaterialDatePicker<Pair<Long, Long>>,reason :String)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_deadline_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateRangePicker=
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

        binding.datepicker.setOnClickListener {

            dateRangePicker.show(requireActivity().supportFragmentManager," ")

        }
//            dateRangePicker.addOnPositiveButtonClickListener {
//                Log.d("12343",""+dateRangePicker.selection!!)
//            }
        binding.submit.setOnClickListener {
            deadlineDialogListener.getDeadline(dateRangePicker
                ,binding.deadline.editText?.text.toString())


        }

    }
    override fun getTheme(): Int {
        return R.style.ThemeOverlay_MaterialComponents
    }

    fun getViewDialog() = binding.root

}