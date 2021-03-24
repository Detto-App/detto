package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.DataBaseOperations
import com.dettoapp.detto.TeacherActivity.db.Classroom
import com.dettoapp.detto.databinding.FragmentClassRoomDetailBinding
import com.dettoapp.detto.databinding.FragmentTeacherHomeBinding

class ClassRoomDetailFrag(
    private val classroom: Classroom,
    private val dataBaseOperations: DataBaseOperations
) : Fragment(), ClassRoomDetailModal.ClassRoomDetailModalClickListener {

    private var _binding: FragmentClassRoomDetailBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentClassRoomDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise(view)
    }

    private fun initialise(view: View) {
        binding.classRoomDetailName.text = classroom.classroomname
        binding.classRoomDetailTeacherName.text = "Demo"

        binding.classRoomDetailMenu.setOnClickListener {
            showBottomDialog()
        }

        view.setOnClickListener {

        }
    }

    private fun showBottomDialog() {
        val bottomSheet = ClassRoomDetailModal(this)
        bottomSheet.show(requireActivity().supportFragmentManager, "classroomModal")
    }

    override fun onDeletePressed() {
        dataBaseOperations.onClassRoomDelete(classroom)
        requireActivity().supportFragmentManager.popBackStack()
    }
}