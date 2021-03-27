package com.dettoapp.detto.StudentActivity.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.R
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.UtilityClasses.Utility

class StudentClassroomAdapter: RecyclerView.Adapter<StudentClassroomAdapter.ClassroomViewHolder>() {


    private val diffCallBack = object : DiffUtil.ItemCallback<Classroom>() {
        override fun areItemsTheSame(oldItem: Classroom, newItem: Classroom): Boolean {
            return oldItem.classroomuid == newItem.classroomuid
        }

        override fun areContentsTheSame(oldItem: Classroom, newItem: Classroom): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomViewHolder {
        return ClassroomViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.studentclassroom_view_holder, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ClassroomViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ClassroomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(classroom: Classroom) {
            val cName = itemView.findViewById<TextView>(R.id.studentclassroomname)
            cName.text = classroom.classroomname
            val teacherName = itemView.findViewById<TextView>(R.id.teachername)
            teacherName.text = classroom.teacher.name


        }
    }
}