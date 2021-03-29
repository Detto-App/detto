package com.dettoapp.detto.StudentActivity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Adapters.ClassroomAdapter

class AddMembersAdapater(private val tName:String,private val classRoomAdapterClickListener: ClassroomAdapter.ClassRoomAdapterClickListener): RecyclerView.Adapter<AddMembersAdapater.MemberViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.student_prject_details_view_holder, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind()

    }
    inner class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(){

        }
    }
}