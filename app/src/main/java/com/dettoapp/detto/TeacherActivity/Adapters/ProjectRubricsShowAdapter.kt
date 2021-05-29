package com.dettoapp.detto.TeacherActivity.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.MarksModel
import com.dettoapp.detto.Models.ProjectRubricsModel
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Utility.toLowerAndTrim
import com.google.android.material.textfield.TextInputLayout

class ProjectRubricsShowAdapter:RecyclerView.Adapter<ProjectRubricsShowAdapter.showRubrics>() {
    private val marksHashMap = HashMap<String, Double>()
    private val marksList=ArrayList<MarksModel>()

    private val diffCallBack = object : DiffUtil.ItemCallback<MarksModel>() {
        override fun areItemsTheSame(
            oldItem: MarksModel,
            newItem: MarksModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MarksModel,
            newItem: MarksModel
        ): Boolean =
            oldItem == newItem


    }
    val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): showRubrics {
        return showRubrics(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.project_rubrics_viewholder, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: showRubrics, position: Int) {

        holder.bind(differ.currentList[position])
    }

    inner class showRubrics(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(marksModel: MarksModel) {
            val title = itemView.findViewById<TextView>(R.id.project_rubrics_title)
            val maxMarks= itemView.findViewById<TextView>(R.id.project_rubrics_maxmarks)
            val convertTo= itemView.findViewById<TextView>(R.id.project_rubrics_converto)
            val marks =itemView.findViewById<TextInputLayout>(R.id.project_rubrics_marks)
            title.setText(marksModel.title)
            maxMarks.setText(marksModel.maxMarks.toString())
            convertTo.setText(marksModel.convertTo.toString())
            marks.editText!!.setText(""+marksModel.marks.toString())
            var newMarksModel:MarksModel=marksModel
            marksHashMap[marksModel.title]=marksModel.marks!!

            marks.editText?.doAfterTextChanged {
//                marksHashMap[marksModel.title]= marks.text.toString().toDouble()
                if(it.isNullOrEmpty()){
                    newMarksModel.marks="0".toString().toDouble()
                }
                else {
                    newMarksModel.marks = marks.editText?.text.toString().toDouble()
                    Log.d("WZZ", newMarksModel.toString())
                }
            }
            marksList.add(newMarksModel)
        }

    }
    fun getMarksHashMap()=marksList

}