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

class ProjectAdapterClassroomDetail:
    RecyclerView.Adapter<ProjectAdapterClassroomDetail.ProjectViewHolder>() {
//    private val diffCallBack = object : DiffUtil.ItemCallback<StudentModel>() {
//        override fun areItemsTheSame(oldItem: StudentModel, newItem: StudentModel): Boolean {
//            return oldItem.susn == newItem.susn
//        }
//
//        override fun areContentsTheSame(oldItem: StudentModel, newItem: StudentModel): Boolean =
//            oldItem == newItem
//
//    }
//    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        return ProjectViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.t_classroom_detail_project_viewholder, parent, false)
        )    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
//        holder.bind(differ.currentList[position])
    }
    inner class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view){
//        fun bind(studentModel: StudentModel) {
//            val sName = itemView.findViewById<TextView>(R.id.studentNameSV)
//            val sUsn = itemView.findViewById<TextView>(R.id.studentUsnSV)
//
//            sName.text = studentModel.name
//            sUsn.text = studentModel.susn.toUpperCase()
//        }
    }
}