package com.dettoapp.detto.TeacherActivity.Adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.R

class ProjectAdapterClassroomDetail :
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
        )
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind()
    }

    inner class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            initialise()
        }

        fun initialise() {
            val accept = itemView.findViewById<TextView>(R.id.acceptProjectClassroom)
            val reject = itemView.findViewById<TextView>(R.id.rejectProjectClassroom)

            accept.setOnClickListener {
                showAlertDialog("Accept", "") {
                    //Log.d("DDDD", "Yess")
                }
            }

            reject.setOnClickListener {
                showAlertDialog("Reject", "") {
                    // Log.d("DDDD", "No")
                }
            }

        }

        private fun showAlertDialog(title: String, message: String, onClickButton: () -> Unit) {
            val dialogBuilder = AlertDialog.Builder(itemView.context)
                    .setMessage(message)
                    .setTitle(title)
                    .setPositiveButton("yes", DialogInterface.OnClickListener { _, _ ->
                        onClickButton()
                    }).setNegativeButton("Cancel") { _, _ ->

                    }.show()
        }
    }
}