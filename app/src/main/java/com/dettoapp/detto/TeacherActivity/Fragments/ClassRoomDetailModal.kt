package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.dettoapp.detto.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ClassRoomDetailModal(private val classRoomDetailModalClickListener: ClassRoomDetailModalClickListener) : BottomSheetDialogFragment() {

    interface ClassRoomDetailModalClickListener
    {
        fun onDeletePressed()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_room_detail_modal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val delete = view.findViewById<TextView>(R.id.classroomModalDelete)
        val deleteImageButton = view.findViewById<ImageButton>(R.id.classRoomDetailModalDeleteButton)

        deleteImageButton.setOnClickListener {
            classRoomDetailModalClickListener.onDeletePressed()
            dismiss()
        }

        delete.setOnClickListener {
            classRoomDetailModalClickListener.onDeletePressed()
            dismiss()
        }
    }

    override fun getTheme(): Int {
        return R.style.Theme_NoWiredStrapInNavigationBar
    }

}