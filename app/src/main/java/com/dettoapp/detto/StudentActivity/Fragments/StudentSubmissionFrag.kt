package com.dettoapp.detto.StudentActivity.Fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.StudentActivity.Adapters.StudentClassroomAdapter
import com.dettoapp.detto.StudentActivity.Adapters.SubmissionAdapter
import com.dettoapp.detto.StudentActivity.Dialog.UploadFileSelectionDialog
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentSubmissionViewModel
import com.dettoapp.detto.StudentActivity.gdrive.UploadGDriveWorker
import com.dettoapp.detto.UtilityClasses.*
import com.dettoapp.detto.databinding.FragmentStudentSubmissionBinding
import java.util.*

class StudentSubmissionFrag :
    BaseFragment<StudentSubmissionViewModel, FragmentStudentSubmissionBinding, StudentRepository>(),
    UploadFileSelectionDialog.UploadFileSelectionInterface {
    private lateinit var submissionAdapter: SubmissionAdapter
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var fileUploadDialog: UploadFileSelectionDialog

    private val readStoragePermissionResult: EasyPermission by requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
        granted = {
            displayView(true)
        }, denied = {
            displayView(false)
        })

    override fun getBaseOnCreate() {
        super.getBaseOnCreate()
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                handleIt(it)
            }
    }

    private fun handleIt(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            data?.let {
                //scheduleUploadWorker(it, viewModel.gDriveToken)
                fileUploadDialog.onFileSelected(viewModel.getFileName(it))
            }
        }
    }

    private fun scheduleUploadWorker(uri: Uri, gDriveToken: String, fileName: String) {

        val data = Data.Builder()
            .putString(UploadGDriveWorker.URI_PATH, uri.toString())
            .putString(UploadGDriveWorker.GDRIVE_TOKEN, gDriveToken)
            .putString(
                UploadGDriveWorker.FOLDER_NAME,
                Utility.STUDENT.name.toUpperCase(Locale.ROOT) + "-" + Utility.STUDENT.susn.toUpperCase(Locale.ROOT)
            )
            .putString(UploadGDriveWorker.FILE_NAME, fileName)
            .build()

        val uploadWorkRequest =
            OneTimeWorkRequestBuilder<UploadGDriveWorker>()
                .setInputData(data)
                .build()

        WorkManager
            .getInstance(requireContext().applicationContext)
            .enqueue(uploadWorkRequest)

        baseActivity.showToast("File Uploading\nCheck Notifications")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        checkPermissions()
        initialise()
        liveDataObservers()
    }

    private fun liveDataObservers() {
        viewModel.submissionFragEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Success -> {
                    when (it.data!!) {
                        "token" -> {
                            binding.fileChooser.isEnabled = true
                            binding.refresh.visibility = View.GONE
                        }
                        else -> {
                            scheduleUploadWorker(viewModel.getFileURI(), viewModel.gDriveToken, it.data)
                            fileUploadDialog.dismiss()
                        }
                    }
                }
                is Resource.Error -> {
                    it.data?.let { errorMessage ->
                        when (errorMessage) {
                            "token" -> {
                                binding.refresh.visibility = View.VISIBLE
                                baseActivity.showErrorSnackMessage(it.message!!)
                            }
                            "dialog" -> {
                                baseActivity.showErrorSnackMessage(it.message!!, fileUploadDialog.getView())
                            }
                        }

                    } ?: baseActivity.showErrorSnackMessage(it.message!!)
                }
                is Resource.Loading -> {
                    baseActivity.showToast("Loading...")
                }
            }
        })
    }

    private fun initialise() {
        binding.enableStoragePermission.setOnClickListener {
            readStoragePermissionResult.launch()
        }
        submissionAdapter = SubmissionAdapter()
        binding.submissionRecyclerview.apply {
            adapter = submissionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.fileChooser.setOnClickListener {
            fileUploadDialog = UploadFileSelectionDialog(requireContext(), this)
            fileUploadDialog.show()
        }
        binding.refresh.setOnClickListener {
            viewModel.getGDriveToken()
        }
    }

    private fun checkPermissions() {
        readStoragePermissionResult.check()
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

    private fun displayView(isShowing: Boolean = false) {
        if (isShowing) {
            binding.enableStoragePermission.visibility = View.GONE

            binding.fileChooser.visibility = View.VISIBLE
            binding.fileName.visibility = View.VISIBLE
        } else {
            binding.enableStoragePermission.visibility = View.VISIBLE

            binding.fileChooser.visibility = View.GONE
            binding.fileName.visibility = View.GONE
        }
    }

    override fun launchFileChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        resultLauncher.launch(intent)
    }

    override fun onUploadButton(modifiedFileName: String) {
        baseActivity.closeKeyBoard(fileUploadDialog.getView())
        viewModel.validate(modifiedFileName)
    }
}