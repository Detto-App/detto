package com.dettoapp.detto.StudentActivity.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.Todo
import com.dettoapp.detto.R

class ToDoAdapter: RecyclerView
.Adapter<ToDoAdapter.TodoViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.toid == newItem.toid
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoAdapter.TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.todo_view_holder, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ToDoAdapter.TodoViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(todo:Todo) {
            Log.d("vikas1",todo.toString())
            val tittle = itemView.findViewById<TextView>(R.id.TodoTitle)
            val category = itemView.findViewById<TextView>(R.id.category)
            val assigned = itemView.findViewById<TextView>(R.id.assigned)

            tittle.text = todo.tittle
            category.text = todo.category
            assigned.text = todo.assignedTo
        }
    }

}