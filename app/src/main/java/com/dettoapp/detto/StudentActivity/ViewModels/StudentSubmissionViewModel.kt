package com.dettoapp.detto.StudentActivity.ViewModels

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.gdrive.CustomProgressListener
import com.dettoapp.detto.StudentActivity.gdrive.DriveServiceHelper
import com.dettoapp.detto.UtilityClasses.BaseViewModel
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.InputStreamContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


@Suppress("BlockingMethodInNonBlockingContext")
@SuppressLint("StaticFieldLeak")
class StudentSubmissionViewModel(
    private val repository: StudentRepository,
    private val applicationContext: Context
) :
    BaseViewModel() {

    lateinit var gDriveToken: String
    init {
        getGDriveToken()
    }

    private fun getGDriveToken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                gDriveToken = repository.getGDriveToken()
            } catch (e: Exception) {
                Log.d("DDSS", "GDRIVE TOKEN Error ${e.localizedMessage}")
            }
        }
    }
}