package com.dettoapp.detto.TeacherActivity.Adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Constants

class ProjectAdapterClassroomDetail(
    private val classroomProjectOperation: ClassroomProjectOperation,
    private val projectAdapterClickListener: ProjectAdapterClickListner
) :
    RecyclerView.Adapter<ProjectAdapterClassroomDetail.ProjectViewHolder>() {
    interface ProjectAdapterClickListner {
        fun OnProjectClicked(projectModel: ProjectModel)
    }

    interface ClassroomProjectOperation {
        fun changeStatus(pid: String, status: String)
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<ProjectModel>() {
        override fun areItemsTheSame(oldItem: ProjectModel, newItem: ProjectModel): Boolean {
            return oldItem.pid == newItem.pid
        }

        override fun areContentsTheSame(oldItem: ProjectModel, newItem: ProjectModel): Boolean =
            oldItem == newItem

    }
    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        return ProjectViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.t_classroom_detail_project_viewholder, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statusAccepted = itemView.findViewById<TextView>(R.id.acceptProjectClassroom)
        val statusRejected = itemView.findViewById<TextView>(R.id.rejectProjectClassroom)
        val statusDisplay = itemView.findViewById<TextView>(R.id.statusDisplay)

        fun bind(projectModel: ProjectModel) {

            initialise(projectModel)
            itemView.setOnClickListener {
                projectAdapterClickListener.OnProjectClicked(projectModel)
            }
        }

        fun initialise(projectModel: ProjectModel) {
            val accept = itemView.findViewById<TextView>(R.id.acceptProjectClassroom)
            val reject = itemView.findViewById<TextView>(R.id.rejectProjectClassroom)
            val projectName = itemView.findViewById<TextView>(R.id.projectName)
            val projectDesc = itemView.findViewById<TextView>(R.id.projectDesc)
            val members=itemView.findViewById<TextView>(R.id.tcdpv_members)
            var names=""
            projectName.text = projectModel.desc
            projectDesc.text = projectModel.title
            val memberslist=projectModel.projectStudentList
            for (i in memberslist)
                names+=i.value.capitalize()+"\n"
            members.text=names.trim()


            if (projectModel.status == Constants.PROJECT_REJECTED)
                changeViewOnClick(Constants.PROJECT_REJECTED, Color.RED)
            else if (projectModel.status == Constants.PROJECT_ACCEPTED)
                changeViewOnClick(Constants.PROJECT_ACCEPTED, Color.GREEN)


            accept.setOnClickListener {
                showAlertDialog("Accept", "") {
                    changeViewOnClick(Constants.PROJECT_ACCEPTED, Color.GREEN)
                    classroomProjectOperation.changeStatus(projectModel.pid, Constants.PROJECT_ACCEPTED)
                }
            }

            reject.setOnClickListener {
                showAlertDialog("Reject", "") {
                    changeViewOnClick(Constants.PROJECT_REJECTED, Color.RED)
                    classroomProjectOperation.changeStatus(projectModel.pid, Constants.PROJECT_REJECTED)
                }
            }

        }

        private fun changeViewOnClick(status: String, color: Int) {
            statusDisplay.text = "Status: ${status}"
            statusDisplay.setBackgroundColor(color)
            statusAccepted.visibility = View.GONE
            statusRejected.visibility = View.GONE
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