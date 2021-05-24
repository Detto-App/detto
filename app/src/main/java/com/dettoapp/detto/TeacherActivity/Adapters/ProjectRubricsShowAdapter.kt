package com.dettoapp.detto.TeacherActivity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.MarksModel
import com.dettoapp.detto.Models.ProjectRubricsModel
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Utility.toLowerAndTrim

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
            val marks =itemView.findViewById<EditText>(R.id.project_rubrics_marks)
            title.setText(marksModel.title)
            maxMarks.setText(marksModel.maxMarks.toString())
            convertTo.setText(marksModel.convertTo.toString())
            marks.setText("")
            var newMarksModel:MarksModel=marksModel
            marksHashMap[marksModel.title]=marksModel.marks!!
            marks.doAfterTextChanged {
//                marksHashMap[marksModel.title]= marks.text.toString().toDouble()

                newMarksModel.marks=marks.text.toString().toDouble()
                marksList.add(newMarksModel)

            }



        }

    }
    fun getMarksHashMap()=marksList

}