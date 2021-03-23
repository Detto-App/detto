package com.dettoapp.detto.TeacherActivity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.db.Classroom

class ClassroomAdapter : RecyclerView.Adapter<ClassroomAdapter.ClassroomViewHolder>() {


    private val diffCallBack = object : DiffUtil.ItemCallback<Classroom>()
    {
        override fun areItemsTheSame(oldItem: Classroom, newItem: Classroom): Boolean {
           return oldItem.classroomuid == newItem.classroomuid
        }

        override fun areContentsTheSame(oldItem: Classroom, newItem: Classroom): Boolean {
            return oldItem == newItem
        }

    }

    val differ  = AsyncListDiffer(this,diffCallBack)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomViewHolder {
        return ClassroomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.classroom_view_holder, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ClassroomViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    class ClassroomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(classroom: Classroom)
        {
            val cName = itemView.findViewById<TextView>(R.id.classroomnameview)
            cName.text = classroom.classroomname
        }
    }
}