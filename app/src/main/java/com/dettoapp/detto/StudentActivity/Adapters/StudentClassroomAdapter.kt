package com.dettoapp.detto.StudentActivity.Adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.R
import com.dettoapp.detto.databinding.StudentclassroomViewHolderBinding

class StudentClassroomAdapter(
    private val studentClassroomAdapterCLickListener: StudentClassroomAdapterCLickListener
) : RecyclerView.Adapter<StudentClassroomAdapter.ClassroomViewHolder>() {


    interface StudentClassroomAdapterCLickListener {
        fun onStudentClassroomClicked(classroom: Classroom)
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
                StudentclassroomViewHolderBinding.bind(LayoutInflater.from(parent.context)
                        .inflate(R.layout.studentclassroom_view_holder, parent, false))
        )
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ClassroomViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ClassroomViewHolder(private val binding:StudentclassroomViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(classroom: Classroom) {
            binding.apply {
                studentClassroomName.text = classroom.classroomname
                teachername.text = classroom.teacher.name

                root.setOnClickListener {
                    studentClassroomAdapterCLickListener.onStudentClassroomClicked(classroom)
                }
            }
        }
    }
}