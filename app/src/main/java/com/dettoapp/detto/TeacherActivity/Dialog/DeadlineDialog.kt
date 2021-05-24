package com.dettoapp.detto.TeacherActivity.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.core.util.Pair
import androidx.fragment.app.DialogFragment
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Constants.toFormattedString
import com.dettoapp.detto.databinding.FragmentDeadlineDialogBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*


class DeadlineDialog(private val deadlineDialogListener: DeadlineDialogListener) : DialogFragment() {

    private val binding: FragmentDeadlineDialogBinding by viewBinding()

    interface DeadlineDialogListener {
        fun getDeadline(dateRangePicker: MaterialDatePicker<Pair<Long, Long>>, reason: String)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_deadline_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select dates")
                        .build()

        binding.datepicker.setOnClickListener {
            dateRangePicker.show(requireActivity().supportFragmentManager, " ")
        }
        binding.submit.setOnClickListener {
            //displaySummaryText()
            deadlineDialogListener.getDeadline(dateRangePicker, binding.deadline.editText?.text.toString())
        }

        dateRangePicker.addOnPositiveButtonClickListener {
            displaySummaryText(dateRangePicker.selection)
            // Respond to positive button click.
        }
    }

    private fun displaySummaryText(selection: Pair<Long, Long>?) {
        var displayString = "Selected Dates are \n\n"

        selection?.let {

            var date = Date(it.first!!)
            displayString += date.toFormattedString("MMM dd YYYY") + " - "

            date = Date(it.second!!)
            displayString += date.toFormattedString("MMM dd YYYY")

            binding.summaryDate.visibility = View.VISIBLE
            binding.summaryDate.text = displayString
        }
    }


    override fun getTheme(): Int {
        return R.style.ThemeOverlay_MaterialComponents
    }

    fun getViewDialog() = binding.root
}