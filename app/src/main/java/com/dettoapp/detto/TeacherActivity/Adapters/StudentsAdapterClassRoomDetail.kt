package com.dettoapp.detto.TeacherActivity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.R

class StudentsAdapterClassRoomDetail :
    RecyclerView.Adapter<StudentsAdapterClassRoomDetail.StudentsViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<StudentModel>() {
        override fun areItemsTheSame(oldItem: StudentModel, newItem: StudentModel): Boolean {
            return oldItem.susn == newItem.susn
        }

        override fun areContentsTheSame(oldItem: StudentModel, newItem: StudentModel): Boolean =
            oldItem == newItem

    }

    val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        return StudentsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.student_viewholder, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class StudentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(studentModel: StudentModel) {
            val sName = itemView.findViewById<TextView>(R.id.studentNameSV)
            val sUsn = itemView.findViewById<TextView>(R.id.studentUsnSV)

            sName.text = studentModel.name
            sUsn.text = studentModel.susn.toUpperCase()
        }
    }
}
