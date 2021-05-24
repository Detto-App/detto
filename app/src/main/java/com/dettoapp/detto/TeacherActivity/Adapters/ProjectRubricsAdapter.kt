package com.dettoapp.detto.TeacherActivity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.MarksModel
import com.dettoapp.detto.Models.ProjectRubricsModel
import com.dettoapp.detto.Models.RubricsModel
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Fragments.ProjectHomeFragment
import com.google.android.material.textfield.TextInputLayout

class ProjectRubricsAdapter:
    RecyclerView.Adapter<ProjectRubricsAdapter.showRubrics>() {
    private val studentHashMap = HashMap<String, ArrayList<MarksModel>>()


    private val diffCallBack = object : DiffUtil.ItemCallback<ProjectRubricsModel>() {

        override fun areItemsTheSame(
            oldItem: ProjectRubricsModel,
            newItem: ProjectRubricsModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ProjectRubricsModel,
            newItem: ProjectRubricsModel
        ): Boolean =
            oldItem == newItem


    }
    val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): showRubrics {
        return showRubrics(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.project_home_viewholder, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: showRubrics, position: Int) {

        holder.bind(differ.currentList[position])
    }

    inner class showRubrics(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(studentNameList: ProjectRubricsModel) {
            val name = itemView.findViewById<TextView>(R.id.student_name)
            val rubrics= itemView.findViewById<RecyclerView>(R.id.project_rubrics)
                name.setText(studentNameList.name)
            var projectRubricsShowAdapter= ProjectRubricsShowAdapter()
            rubrics.apply {
                adapter=projectRubricsShowAdapter
                layoutManager = LinearLayoutManager(itemView.context)
            }
            projectRubricsShowAdapter.differ.submitList(studentNameList.rubrics.titleMarksList)
            val marksList=projectRubricsShowAdapter.getMarksHashMap()
            studentHashMap[studentNameList.usn]=marksList




        }

    }
    fun getStudentHashMap()=studentHashMap
}