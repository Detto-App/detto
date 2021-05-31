package com.dettoapp.detto.TeacherActivity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.MarksModel
import com.dettoapp.detto.R
import com.dettoapp.detto.databinding.RubricsViewholderBinding
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class RubricsShowAdapter :
        RecyclerView.Adapter<RubricsShowAdapter.RubricsViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<MarksModel>() {
        override fun areItemsTheSame(oldItem: MarksModel, newItem: MarksModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MarksModel, newItem: MarksModel): Boolean =
                oldItem == newItem
    }


    val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RubricsViewHolder {
        return RubricsViewHolder(RubricsViewholderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RubricsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class RubricsViewHolder(private val binding: RubricsViewholderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(marksModel: MarksModel) {
            val title = itemView.findViewById<TextInputLayout>(R.id.rubrics_title)
            val marks = itemView.findViewById<TextInputLayout>(R.id.rubrics_marks)
            //val slno = itemView.findViewById<TextInputLayout>(R.id.slno)
            val convertTo = itemView.findViewById<TextInputLayout>(R.id.convertTo)
            title.editText?.isFocusable = false
            marks.editText?.isFocusable = false
            convertTo.editText?.isFocusable = false
            binding.slno.isFocusable = false



            title.editText?.setText(marksModel.title.capitalize(Locale.ROOT))
            marks.editText?.setText(marksModel.maxMarks.toString())
            convertTo.editText?.setText(marksModel.convertTo.toString())
            binding.slno.setText((adapterPosition + 1).toString())
        }
    }
}