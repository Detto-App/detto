package com.dettoapp.detto.StudentActivity.ViewModels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.gdrive.DriveServiceHelper
import com.dettoapp.detto.UtilityClasses.BaseViewModel
import com.dettoapp.detto.UtilityClasses.Utility
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class StudentSubmissionViewModel(private val repository: StudentRepository) :
    BaseViewModel() {

    private lateinit var driveServiceHelper: DriveServiceHelper
    private lateinit var folder: File
    var defaultFolderName = Utility.STUDENT.uid

    init {
        getGDriveToken()
    }

    private fun getGDriveToken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val gDriveToken = repository.getGDriveToken()
                initialiseDriveService(gDriveToken)
                getIndividualFolderID()
            } catch (e: Exception) {
                Log.d("DDSS", "GDRIVE TOKEN Error ${e.localizedMessage}")
            }
        }
    }

    private fun getIndividualFolderID() {
        folder =
            driveServiceHelper.createOrGetFolderReference(defaultFolderName)
        Log.d("DDSS", folder.id)
    }

    private fun initialiseDriveService(gDriveToken: String) {
        val drive = Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            getGoogleCredential(gDriveToken)
        ).build()
        driveServiceHelper = DriveServiceHelper(drive)
    }

    private fun getGoogleCredential(gDriveToken: String): GoogleCredential {
        val credential = GoogleCredential()
        credential.accessToken = gDriveToken
        return credential
    }
}