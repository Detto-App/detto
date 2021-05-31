package com.dettoapp.detto.TeacherActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.TeacherActivity.Adapters.RubricsAdapter
import com.dettoapp.detto.TeacherActivity.Adapters.RubricsShowAdapter
import com.dettoapp.detto.TeacherActivity.Dialog.RubricsDialog
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentRubricsBinding

class RubricsFragment(private val operations: ClassroomDetailOperations) :
    BaseFragment<ClassRoomDetailViewModel, FragmentRubricsBinding, ClassroomDetailRepository>(),
    RubricsDialog.RubricsDialogListener {


    private lateinit var rDialog: RubricsDialog
    private lateinit var rubricsAdapter: RubricsAdapter
    private lateinit var rubricsShowAdapter: RubricsShowAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        liveDataObservers()
    }

    private fun initialise() {
        rubricsShowAdapter = RubricsShowAdapter()
        binding.rubricsShow.apply {
            adapter = rubricsShowAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.btnRubrics.setOnClickListener {
            rDialog = RubricsDialog(this)
            rDialog.show(requireActivity().supportFragmentManager, "dhsa")
        }
        viewModel.getRubrics(operations.getClassroom())
    }

    fun liveDataObservers() {
        viewModel.rubrics.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.hideProgressDialog()
//                    binding.re.isRefreshing = false
//                    baseActivity.showToast("done")
                    binding.btnRubrics.visibility = View.GONE
                }
                is Resource.Error -> {
//                    baseActivity.showErrorSnackMessage(it.message!!)
                    binding.norubrics.visibility=View.VISIBLE
                }
                is Resource.Loading -> {
//                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
                }
                is Resource.Confirm -> {
                    baseActivity.hideProgressDialog()
                }
                else -> {
                }
            }
        })
        viewModel.marks.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.hideProgressDialog()
//                    binding.re.isRefreshing = false
//                    baseActivity.showToast("done")
                    binding.btnRubrics.visibility = View.GONE


                    rubricsShowAdapter.differ.submitList(it.data)

                }
                is Resource.Error -> {
//                    baseActivity.showErrorSnackMessage(it.message!!)
                    binding.norubrics.visibility=View.VISIBLE
                }
                is Resource.Loading -> {
//                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
                }
                is Resource.Confirm -> {
                    baseActivity.hideProgressDialog()
                }
                else -> {
                }
            }
        })


    }


    override fun getViewModelClass(): Class<ClassRoomDetailViewModel> {
        return ClassRoomDetailViewModel::class.java

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRubricsBinding {
        return FragmentRubricsBinding.inflate(inflater, container, false)

    }

    override fun getRepository(): ClassroomDetailRepository {
        return ClassroomDetailRepository(DatabaseDetto.getInstance(requireContext().applicationContext).rubricsDAO)

    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBaseDestroy() {
        super.onBaseDestroy()
    }

    override fun setRubrics(
        titleMap: HashMap<Int, String>,
        marksMap: HashMap<Int, Int>,
        convertMap: HashMap<Int, Int>
    ) {
        viewModel.storeRubrics(titleMap, marksMap, convertMap, operations.getClassroom())
        initialise()
        liveDataObservers()
        rDialog.dismiss()

    }


    override fun getBaseViewModelOwner(): ViewModelStoreOwner {
        return operations.getViewModelStoreOwner()
    }
}