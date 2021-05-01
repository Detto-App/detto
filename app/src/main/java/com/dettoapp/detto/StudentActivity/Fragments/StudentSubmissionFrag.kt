package com.dettoapp.detto.StudentActivity.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentSubmissionViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.databinding.FragmentStudentSubmissionBinding

class StudentSubmissionFrag :
    BaseFragment<StudentSubmissionViewModel, FragmentStudentSubmissionBinding, StudentRepository>() {


    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun getBaseOnCreate() {
        super.getBaseOnCreate()
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                handleIt(it)
            }
    }

    private fun handleIt(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data?.data
            binding.fileName.text = "" + data

            data?.let {
                viewModel.uploadFile(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fileChooser.setOnClickListener {
            val intent2 = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent2.addCategory(Intent.CATEGORY_OPENABLE)
            intent2.setType("*/*")
            //intent.type = "text/plain"
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                // Optionally, specify a URI for the directory that should be opened in
                // the system file picker when it loads.
                //putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }

            resultLauncher.launch(intent2)
        }

        viewModel.x.observe(viewLifecycleOwner,{
            binding.fileName.text = it
        })
    }

    override fun getViewModelClass(): Class<StudentSubmissionViewModel> {
        return StudentSubmissionViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStudentSubmissionBinding {
        return FragmentStudentSubmissionBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): StudentRepository {
        return StudentRepository(
            DatabaseDetto.getInstance(requireContext().applicationContext).classroomDAO,
            DatabaseDetto.getInstance(requireContext().applicationContext).projectDAO
        )
    }
}