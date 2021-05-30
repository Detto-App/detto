package com.dettoapp.detto.TeacherActivity.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.R
import com.dettoapp.detto.databinding.ClassDetailOptionsMenuCellBinding

class ClassDetailOptionsAdapter(private val finalList: ArrayList<String>,private val classDetailOptionsInterface: ClassDetailOptionsInterface ) : RecyclerView.Adapter<ClassDetailOptionsAdapter.ClassDetailOptionViewHolder>() {

    interface ClassDetailOptionsInterface
    {
        fun onOptionClicked(type:String)
    }

    inner class ClassDetailOptionViewHolder(private val binding: ClassDetailOptionsMenuCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            binding.optionTitle.text = data

            itemView.setOnClickListener {
                classDetailOptionsInterface.onOptionClicked(data)
            }

            when(data)
            {
                "Deadlines" -> {
                    binding.optionImage.setImageResource(R.drawable.ic_baseline_notifications_active_24)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassDetailOptionViewHolder {
        return ClassDetailOptionViewHolder(ClassDetailOptionsMenuCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ClassDetailOptionViewHolder, position: Int) {
        holder.bind(finalList[position])
    }

    override fun getItemCount(): Int {
        return finalList.size
    }
}