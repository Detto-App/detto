package com.dettoapp.detto.StudentActivity.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.R
import com.dettoapp.detto.Models.Classroom

class StudentClassroomAdapter(private val adapterAndFrag:StudentClassroomAdapter.AdapterAndFrag): RecyclerView.Adapter<StudentClassroomAdapter.ClassroomViewHolder>() {


    interface AdapterAndFrag{
        fun communicate()
    }
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
            val cName = itemView.findViewById<TextView>(R.id.tcdpv_project_name)
            cName.text = classroom.classroomname
            val teacherName = itemView.findViewById<TextView>(R.id.teachername)
            teacherName.text = classroom.teacher.name

            itemView.setOnClickListener{
                adapterAndFrag.communicate()
                Toast.makeText(itemView.context,"vikas ainapur", Toast.LENGTH_SHORT).show()
            }
        }
    }
}