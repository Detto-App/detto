package com.dettoapp.detto.StudentActivity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Utility.toLowerAndTrim
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashSet

@Suppress("PrivatePropertyName")
class AddMembersAdapter(classroom: Classroom) : RecyclerView.Adapter<AddMembersAdapter.MemberViewHolder>() {

    private val memberList = ArrayList<String>()
    private val valueHashMap = HashMap<Int, String>()
    private var MAX_TEAM_SIZE = classroom.settingsModel.teamSize.toInt()

    private val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.student_prject_details_view_holder, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position)

    }

    inner class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(value: String, position: Int) {
            val usn = itemView.findViewById<TextInputLayout>(R.id.usnStudentProject)
            usn.hint = "USN Of Member $value"
            usn.editText?.setText("")
            usn.editText?.doAfterTextChanged {
                valueHashMap[position] = usn.editText?.text.toString().toLowerAndTrim()
            }
        }
    }

    fun addOption() {
        if (memberList.size > MAX_TEAM_SIZE - 1)
            return
        memberList.add("" + (memberList.size + 1))
        valueHashMap[(memberList.size - 1)] = ""
        differ.submitList(ArrayList(memberList))
    }

    fun minusOption() {
        if (memberList.size == 0)
            return
        valueHashMap.remove(memberList.size - 1)
        memberList.removeAt(memberList.size - 1)
        differ.submitList(ArrayList(memberList))
    }

    fun getUsnMap() = valueHashMap

    fun getArrayList() = memberList
}