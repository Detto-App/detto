package com.dettoapp.detto.TeacherActivity.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.Adapters.AddMembersAdapter
import com.dettoapp.detto.TeacherActivity.Adapters.RubricsAdapter
import com.dettoapp.detto.databinding.DialogAddRubricsBinding

class RubricsDialog(private val rubricsDialogListener:RubricsDialogListener): DialogFragment() {
    private val binding: DialogAddRubricsBinding by viewBinding()

    interface RubricsDialogListener {
        fun setRubrics(titleMap:HashMap<Int,String>,marksMap:HashMap<Int,Int>,convertMap:HashMap<Int,Int>)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_rubrics, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterLocal = RubricsAdapter()
        binding.Rubrics.apply {
            adapter = adapterLocal
            layoutManager = LinearLayoutManager(view.context)
        }

        binding.addProjectUsn2.setOnClickListener {
            adapterLocal.addOption()
        }

        binding.minusProjectUsn2.setOnClickListener {
            adapterLocal.minusOption()
        }

        binding.done.setOnClickListener {
            rubricsDialogListener.setRubrics(
                adapterLocal.gettitleMap(),
                adapterLocal.getMarksMap(),
                adapterLocal.getConvertHashMap()
            )
        }
    }
    override fun getTheme(): Int {
        return R.style.ThemeOverlay_MaterialComponents
    }

}