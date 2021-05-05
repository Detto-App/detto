package com.dettoapp.detto.StudentActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.UtilityClasses.BaseViewModel
import com.dettoapp.detto.UtilityClasses.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Suppress("BlockingMethodInNonBlockingContext")
@SuppressLint("StaticFieldLeak")
class StudentSubmissionViewModel(
        private val repository: StudentRepository,
        private val applicationContext: Context
) :
        BaseViewModel() {

    var gDriveToken: String = "nothing"

    private lateinit var tempURI: Uri

    private val _submissionFragEvent = MutableLiveData<Resource<String>>()
    val submissionFragEvent: LiveData<Resource<String>>
        get() = _submissionFragEvent

    init {
        getGDriveToken()
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


    fun getFileURI() = tempURI

    fun validate(fileName: String) {
        _submissionFragEvent.postValue(Resource.Success(data = fileName))
    }
}