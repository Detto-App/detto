package com.dettoapp.detto.StudentActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.githubModels.SubmissionModel
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.BaseViewModel
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


@Suppress("BlockingMethodInNonBlockingContext")
@SuppressLint("StaticFieldLeak")
class StudentSubmissionViewModel(
        private val repository: StudentRepository,
        private val applicationContext: Context
) :
        BaseViewModel() {

    var gDriveToken: String = "nothing"

    lateinit var pid: String

    private lateinit var tempURI: Uri

    private val _submissionFragEvent = MutableLiveData<Resource<String>>()
    val submissionFragEvent: LiveData<Resource<String>>
        get() = _submissionFragEvent

    private val _uploadFiles = MutableLiveData<Resource<List<SubmissionModel>>>()
    val uploadFiles: LiveData<Resource<List<SubmissionModel>>>
        get() = _uploadFiles

    init {
        getGDriveToken()
    }

    fun getUploadedFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uploadFiles.postValue(Resource.Loading())
                val list = RetrofitInstance.submissionAPI.getUploadedFiles(pid).body()
                        ?: throw Exception("Unknown Error")
                _uploadFiles.postValue(Resource.Success(data = list))
            } catch (e: Exception) {
                _uploadFiles.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun getGDriveToken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _submissionFragEvent.postValue(Resource.Loading())
                gDriveToken = repository.getGDriveToken()
                _submissionFragEvent.postValue(Resource.Success("token"))
            } catch (e: Exception) {
                _submissionFragEvent.postValue(Resource.Error(message = "Unable to Connect", data = "token"))
            }
        }
    }

    fun getFileName(fileUri: Uri): String {
        tempURI = fileUri
        var name = ""
        val returnCursor = applicationContext.contentResolver.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }

    fun getPID(cid: String, context: Context) {
        val sharedPreference =
                context.getSharedPreferences(Constants.CLASS_PROJECT, Context.MODE_PRIVATE)
                        ?: throw Exception("Data Storage Exception")
        pid = sharedPreference.getString(cid, "")!!
    }

    fun getFileURI() = tempURI

    fun validate(fileName: String) {
        viewModelScope.launch {
            try {
                val localFileName = fileName.trim().capitalize(Locale.ROOT)
                if (localFileName.isEmpty() || localFileName.length <= 4)
                    throw Exception("Bad File Name")
                _submissionFragEvent.postValue(Resource.Success(data = fileName))
            } catch (e: Exception) {
                _submissionFragEvent.postValue(Resource.Error(message = "" + e.localizedMessage, data = "dialog"))
            }
        }
    }
}