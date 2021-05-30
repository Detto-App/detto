package com.dettoapp.detto.StudentActivity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Utility.toLowerAndTrim
import com.google.android.material.textfield.TextInputLayout

class SubmissionAdapter():RecyclerView.Adapter<SubmissionAdapter.SubmissionRecyclerViewHolder>() {
    private val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionAdapter.SubmissionRecyclerViewHolder {
        return SubmissionRecyclerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.submission_view_holder, parent, false)
        )
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SubmissionAdapter.SubmissionRecyclerViewHolder, position: Int) {
        holder.bind()

    }
    inner class SubmissionRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {

            }
        }
    }
