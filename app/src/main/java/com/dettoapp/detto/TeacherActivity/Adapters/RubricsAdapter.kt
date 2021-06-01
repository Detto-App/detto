package com.dettoapp.detto.TeacherActivity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Utility.toLowerAndTrim
import com.dettoapp.detto.databinding.RubricsViewholderBinding
import com.google.android.material.textfield.TextInputLayout

class RubricsAdapter() : RecyclerView.Adapter<RubricsAdapter.addRubrics>() {
    private val rubricsList = ArrayList<String>()
    private val valueHashMap = HashMap<Int, String>()
    private val marksHashMap = HashMap<Int, Int>()
    private val convertHashMap = HashMap<Int, Int>()

    private val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): addRubrics {
        return addRubrics(RubricsViewholderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: addRubrics, position: Int) {
        holder.bind(differ.currentList[position], position)

    }

    inner class addRubrics(private val binding: RubricsViewholderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(value: String, position: Int) {
            val title = itemView.findViewById<TextInputLayout>(R.id.rubrics_title)
            val marks = itemView.findViewById<TextInputLayout>(R.id.rubrics_marks)
            //val slno = itemView.findViewById<TextInputLayout>(R.id.slno)
            val convert = itemView.findViewById<TextInputLayout>(R.id.convertTo)
            binding.slno.text = "$value"
            title.editText?.setText("")
            marks.editText?.setText("")
            title.editText?.doAfterTextChanged {
                valueHashMap[position] = title.editText?.text.toString().toLowerAndTrim()

            }
            marks.editText?.doAfterTextChanged {
                if (marks.editText?.text.toString().isNotEmpty())
                    marksHashMap[position] = marks.editText?.text.toString().toInt()
            }
            convert.editText?.doAfterTextChanged {
                if (marks.editText?.text.toString().isNotEmpty())
                    convertHashMap[position] = convert.editText?.text.toString().toInt()
            }
        }
    }

    fun addOption() {
        rubricsList.add("" + (rubricsList.size + 1))
        valueHashMap[(rubricsList.size - 1)] = ""
        marksHashMap[(rubricsList.size - 1)] = 0
        convertHashMap[(rubricsList.size - 1)] = 0
        differ.submitList(ArrayList(rubricsList))
    }

    fun minusOption() {
        if (rubricsList.size == 0)
            return
        valueHashMap.remove(rubricsList.size - 1)
        marksHashMap.remove(rubricsList.size - 1)
        rubricsList.removeAt(rubricsList.size - 1)
        convertHashMap.remove(rubricsList.size - 1)
        differ.submitList(ArrayList(rubricsList))
    }

    fun gettitleMap() = valueHashMap
    fun getMarksMap() = marksHashMap
    fun getConvertHashMap() = convertHashMap

    fun getArrayList() = rubricsList

}