package com.dettoapp.detto.TeacherActivity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.R

class ClassroomAdapter : RecyclerView.Adapter<ClassroomAdapter.ClassroomViewHolder>(){
    class ClassroomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomViewHolder {
        return ClassroomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.classroom_view_holder, parent, false))
    }


    override fun getItemCount(): Int {
        return 5;
    }

    override fun onBindViewHolder(holder: ClassroomViewHolder, position: Int) {

    }
}