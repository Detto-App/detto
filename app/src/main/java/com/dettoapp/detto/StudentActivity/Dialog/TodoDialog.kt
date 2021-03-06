package com.dettoapp.detto.StudentActivity.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.databinding.FragmentTodoDialogBinding

class TodoDialog(
    private val todoDialogListener: TodoDialogListener, context: Context, private val roles: List<String>
) : Dialog(context, R.style.CustomDialog) {

    private var _binding: FragmentTodoDialogBinding? = null
    private val binding: FragmentTodoDialogBinding
        get() = _binding!!

    interface TodoDialogListener {
        fun createTodo(tittle: String, category: String, assigned: String, status: Int)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentTodoDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitTask.setOnClickListener {
            todoDialogListener.createTodo(
                binding.todoTitle.editText?.text.toString(),
                binding.todoCategory.editText?.text.toString(),
                binding.assignedDialog.text.toString(), Constants.NOTDONE
            )

        }
        val adapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, roles)
        binding.assignedDialog.setAdapter(adapter)

    }

    fun getViewDialog() = binding.root
}