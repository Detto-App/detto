package com.dettoapp.detto.TeacherActivity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.DeadlineModel
import com.dettoapp.detto.R

class DeadlineAdapterClassroomDetail :
    RecyclerView.Adapter<DeadlineAdapterClassroomDetail.DeadlineViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<DeadlineModel>() {
        override fun areItemsTheSame(oldItem: DeadlineModel, newItem: DeadlineModel): Boolean {
            return oldItem.did == newItem.did
        }

        override fun areContentsTheSame(oldItem: DeadlineModel, newItem: DeadlineModel): Boolean =
            oldItem == newItem

    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeadlineAdapterClassroomDetail.DeadlineViewHolder {
        return DeadlineViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.deadline_viewholder, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
//        return 2
    }

    override fun onBindViewHolder(holder: DeadlineAdapterClassroomDetail.DeadlineViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class DeadlineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(deadlineModel: DeadlineModel) {
            val reason = itemView.findViewById<TextView>(R.id.reason_text)
            val date = itemView.findViewById<TextView>(R.id.deadline_date)

            reason.text = deadlineModel.description
            date.text = deadlineModel.fromdate
        }
    }


}
