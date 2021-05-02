package com.dettoapp.detto.StudentActivity.Fragments

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
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentSubmissionViewModel
import com.dettoapp.detto.StudentActivity.gdrive.UploadGDriveWorker
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Utility
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
            val data = result.data?.data
            data?.let {
                scheduleUploadWorker(it, viewModel.gDriveToken)
            }
        }
    }

    private fun scheduleUploadWorker(uri: Uri, gDriveToken: String) {

        val data = Data.Builder()
            .putString(UploadGDriveWorker.URI_PATH, uri.toString())
            .putString(UploadGDriveWorker.GDRIVE_TOKEN, gDriveToken)
            .putString(UploadGDriveWorker.FOLDER_NAME,Utility.STUDENT.uid)
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

        binding.fileChooser.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"

            resultLauncher.launch(intent)
        }
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