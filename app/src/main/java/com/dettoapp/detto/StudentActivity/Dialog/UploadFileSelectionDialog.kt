package com.dettoapp.detto.StudentActivity.Dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import com.dettoapp.detto.R
import com.dettoapp.detto.databinding.DialogUploadFileSelectionBinding

class UploadFileSelectionDialog(context: Context, private val uploadFileSelectionInterface: UploadFileSelectionInterface
) : Dialog(context, R.style.CustomDialog) {
    private var _binding: DialogUploadFileSelectionBinding? = null
    private val binding: DialogUploadFileSelectionBinding
        get() = _binding!!

    interface UploadFileSelectionInterface {
        fun launchFileChooser()
        fun onUploadButton(modifiedFileName: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DialogUploadFileSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialise()
    }

    private fun initialise() {
        binding.fileChooser.setOnClickListener {
            uploadFileSelectionInterface.launchFileChooser()
        }

        binding.upload.setOnClickListener {
            uploadFileSelectionInterface.onUploadButton(binding.fileName.editText!!.text.toString())
        }
    }


    fun onFileSelected(fileName: String) {
        binding.fileUploadGroup.visibility = View.VISIBLE
        binding.fileName.editText?.setText(fileName)
    }

    fun getView() = binding.root
}