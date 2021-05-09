package com.dettoapp.detto.StudentActivity.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.dettoapp.detto.R
import com.dettoapp.detto.databinding.FragmentTodoBinding
import com.dettoapp.detto.databinding.FragmentTodoDialogBinding

class TodoDialog(private val todoDialogListener: TodoDialog.TodoDialogListener): DialogFragment() {
    private val binding: FragmentTodoDialogBinding by viewBinding()

    interface TodoDialogListener {
        fun createTodo(tittle:String , category :String , assigned:String)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitTask.setOnClickListener {
            //displaySummaryText()
            todoDialogListener.createTodo(binding.tittle.editText?.text.toString(),
                                          binding.categoryEditText.editText?.text.toString(),
                                          binding.assigned.textAlignment.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_dialog, container, false)
    }

    override fun getTheme(): Int {
        return R.style.ThemeOverlay_MaterialComponents
    }
    fun getViewDialog() = binding.root

}