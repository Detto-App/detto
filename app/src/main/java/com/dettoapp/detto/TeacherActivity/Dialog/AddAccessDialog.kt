package com.dettoapp.detto.TeacherActivity.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.dettoapp.detto.R
import com.dettoapp.detto.databinding.DialogAddAccessBinding

class AddAccessDialog(private val addAccessDialogListener: AddAccessDialogListener) : DialogFragment() {
    private val binding: DialogAddAccessBinding by viewBinding()

    interface AddAccessDialogListener {
        fun addAccess(access: String, sem: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_access, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sem = resources.getStringArray(R.array.sem)
        val access = resources.getStringArray(R.array.access)


        val semAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sem)
        val accessAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, access)

        binding.chooseSem.setAdapter(semAdapter)
        binding.chooseAccess.setAdapter(accessAdapter)
        binding.addAccess.setOnClickListener {
            val accessSelected = binding.chooseAccess.text.toString()
            val semSelected = binding.chooseSem.text.toString()
            addAccessDialogListener.addAccess(accessSelected, semSelected)
        }
    }

    override fun getTheme(): Int {
        return R.style.ThemeOverlay_MaterialComponents
    }

}