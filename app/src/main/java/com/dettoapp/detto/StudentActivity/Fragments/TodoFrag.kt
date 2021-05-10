package com.dettoapp.detto.StudentActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.StudentActivity.Adapters.ToDoAdapter
import com.dettoapp.detto.StudentActivity.Dialog.TodoDialog
import com.dettoapp.detto.StudentActivity.StudentOperations
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.TodoViewModel
import com.dettoapp.detto.TeacherActivity.Adapters.DeadlineAdapterClassroomDetail
import com.dettoapp.detto.TeacherActivity.Dialog.DeadlineDialog
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentTodoBinding


class TodoFrag(private val projectModel: ProjectModel, private val studentOperations: StudentOperations) :
    BaseFragment<TodoViewModel, FragmentTodoBinding, StudentRepository>() ,
    TodoDialog.TodoDialogListener  {

    private lateinit var todoAdapter: ToDoAdapter
    private lateinit var tDialog: TodoDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()
    }

    fun initialise(){
        todoAdapter = ToDoAdapter()
        binding.recyclerView.apply {
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.addtodo.setOnClickListener {
            tDialog = TodoDialog(this)
            tDialog.show(requireActivity().supportFragmentManager, "dhsa")
        }
        viewModel.getTodo(projectModel.pid)


    }

    fun liveDataObservers(){
        viewModel.todo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.todoprogressBar.visibility = View.GONE
//                    binding.re.isRefreshing = false
                    todoAdapter.differ.submitList(it.data)
                }
                is Resource.Error -> {
                    baseActivity.showErrorSnackMessage(it.message!!)
                }
                is Resource.Loading -> {
                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
                }
                is Resource.Confirm ->{
                    baseActivity.hideProgressDialog()
                }
                else -> {
                }
            }
        })
    }

    override fun getViewModelClass(): Class<TodoViewModel> {
        return TodoViewModel::class.java
    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTodoBinding {
        return FragmentTodoBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): StudentRepository {
        return StudentRepository(
            DatabaseDetto.getInstance(requireContext().applicationContext).classroomDAO,
            DatabaseDetto.getInstance(requireContext().applicationContext).projectDAO
        )
    }

    override fun createTodo(tittle:String , category :String , assigned:String) {
        viewModel.createTodo(projectModel.pid,tittle,category,assigned)
        tDialog.dismiss()
    }


}