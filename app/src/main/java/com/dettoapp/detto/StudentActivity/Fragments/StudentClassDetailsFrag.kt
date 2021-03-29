package com.dettoapp.detto.StudentActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.Dialog.ProjectDetailsDialog
import com.dettoapp.detto.databinding.FragmentLoginBinding
import com.dettoapp.detto.databinding.FragmentStudentClassDetailsBinding


class StudentClassDetailsFrag : Fragment() {
    private var _binding: FragmentStudentClassDetailsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStudentClassDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.stuClassDetailsbutton.setOnClickListener {
            val dialog=ProjectDetailsDialog()
            dialog.show(requireActivity().supportFragmentManager,"jdslfj")
        }
        view.setOnClickListener {  }

    }
}