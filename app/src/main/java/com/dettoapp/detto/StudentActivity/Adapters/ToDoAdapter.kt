package com.dettoapp.detto.StudentActivity.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.Todo
import com.dettoapp.detto.R

class ToDoAdapter(private val todoOperation: TodoOperation) : RecyclerView
.Adapter<ToDoAdapter.TodoViewHolder>() {

    interface TodoOperation {
        fun deleteTodo(toid: String)
        fun reload()
        fun changeStatus(toid: String)
    }

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
        fun bind(todo: Todo) {
            val tittle = itemView.findViewById<TextView>(R.id.TodoTitle)
            val category = itemView.findViewById<TextView>(R.id.category)
            val assigned = itemView.findViewById<TextView>(R.id.todoassigned)
            val tododelete = itemView.findViewById<ImageButton>(R.id.tododelete)
            val todostatus = itemView.findViewById<ImageButton>(R.id.todostatus)


            tittle.text = todo.tittle
            category.text = todo.category
            assigned.text = todo.assigned_to

            if (todo.status == 0) {
                itemView.findViewById<ConstraintLayout>(R.id.todolayout).setBackgroundColor(Color.GREEN)
                itemView.findViewById<ImageButton>(R.id.todostatus).visibility=View.INVISIBLE
            }

            tododelete.setOnClickListener {
                todoOperation.reload()
                todoOperation.deleteTodo(todo.toid)
            }

            todostatus.setOnClickListener {
                todoOperation.changeStatus(todo.toid)
                itemView.findViewById<ConstraintLayout>(R.id.todolayout).setBackgroundColor(Color.GREEN)
                todostatus.isEnabled = false
                itemView.findViewById<ImageButton>(R.id.todostatus).visibility=View.INVISIBLE
            }

        }
    }

}