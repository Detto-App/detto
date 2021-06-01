package com.dettoapp.detto.StudentActivity.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.Timeline
import com.dettoapp.detto.R

class TimelineAdapter() : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {


    private val diffCallBack = object : DiffUtil.ItemCallback<Timeline>() {
        override fun areItemsTheSame(oldItem: Timeline, newItem: Timeline): Boolean {
            return oldItem.tiid == newItem.tiid
        }

        override fun areContentsTheSame(oldItem: Timeline, newItem: Timeline): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        return TimelineViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.timeline_viewholder, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(timeline: Timeline) {
            val tittle = itemView.findViewById<TextView>(R.id.tittle)
            val date = itemView.findViewById<TextView>(R.id.date)
            val assigned = itemView.findViewById<TextView>(R.id.assigened_to)
            Log.d("123", "1")
            tittle.text = timeline.tittle
            date.text = timeline.date
            assigned.text = timeline.assigned_to

        }
    }

}