package com.dettoapp.detto.TeacherActivity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.MarksModel
import com.dettoapp.detto.Models.RubricsModel
import com.dettoapp.detto.Models.StudentModel
import com.dettoapp.detto.R
import com.google.android.material.textfield.TextInputLayout

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
            return RubricsViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.rubrics_viewholder, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RubricsViewHolder, position: Int) {
            holder.bind(differ.currentList[position])
        }

        override fun getItemCount(): Int {
            return differ.currentList.size
        }

        inner class RubricsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(marksModel: MarksModel) {
                val title = itemView.findViewById<TextInputLayout>(R.id.rubrics_title)
                val marks = itemView.findViewById<TextInputLayout>(R.id.rubrics_marks)
                val slno =itemView.findViewById<TextInputLayout>(R.id.slno)
                val convertTo=itemView.findViewById<TextInputLayout>(R.id.convertTo)
                title.isEnabled=false
                marks.isEnabled=false
                convertTo.isEnabled=false
                slno.isEnabled=false



                title.editText?.setText(marksModel.title.toString())
                marks.editText?.setText(marksModel.maxMarks.toString())
                convertTo.editText?.setText(marksModel.convertTo.toString())
                slno.editText?.setText((adapterPosition+1).toString())
            }
        }
}