package com.dettoapp.detto.TeacherActivity.Adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.R
import com.dettoapp.detto.Models.Classroom

class ClassroomAdapter(private val tName:String,private val classRoomAdapterClickListener: ClassRoomAdapterClickListener): RecyclerView.Adapter<ClassroomAdapter.ClassroomViewHolder>() {

    interface ClassRoomAdapterClickListener
    {
        fun onClassRoomCLicked(classroom: Classroom)
    }


    private val diffCallBack = object : DiffUtil.ItemCallback<Classroom>()
    {
        override fun areItemsTheSame(oldItem: Classroom, newItem: Classroom): Boolean {
           return oldItem.classroomuid == newItem.classroomuid
        }

        override fun areContentsTheSame(oldItem: Classroom, newItem: Classroom): Boolean {
            return oldItem == newItem
        }

    }

    val differ  = AsyncListDiffer(this,diffCallBack)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomViewHolder {
        return ClassroomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.classroom_view_holder, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ClassroomViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ClassroomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(classroom: Classroom)
        {
            val cName = itemView.findViewById<TextView>(R.id.classroomnameview)
            cName.text = classroom.classroomname
            val teacherName=itemView.findViewById<TextView>(R.id.teachernameview)
            teacherName.text=tName

            val link = itemView.findViewById<TextView>(R.id.classLink)
            link.setOnClickListener {
                itemView.context.copyToClipboard("https://detto.uk.to/cid/"+classroom.classroomuid)
                Toast.makeText(itemView.context, "Copied", Toast.LENGTH_SHORT).show()
            }

            itemView.setOnClickListener {
                classRoomAdapterClickListener.onClassRoomCLicked(classroom)
            }
        }
    }

    fun Context.copyToClipboard(text: CharSequence){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",text)
        clipboard.setPrimaryClip(clip)
    }
}