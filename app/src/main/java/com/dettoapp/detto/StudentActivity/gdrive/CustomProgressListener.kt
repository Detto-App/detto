package com.dettoapp.detto.StudentActivity.gdrive

import com.dettoapp.detto.UtilityClasses.Resource
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CustomProgressListener :
        MediaHttpUploaderProgressListener {

    val progressUpdates = MutableStateFlow<Resource<String>>(Resource.Success(data = "0"))

    override fun progressChanged(uploader: MediaHttpUploader?) {
        CoroutineScope(Dispatchers.Default).launch {
            when (uploader?.uploadState) {
                MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS -> {
                    progressUpdates.emit(Resource.Success(data = uploader.progress.toString()))
                }
                MediaHttpUploader.UploadState.MEDIA_COMPLETE -> {
                    progressUpdates.emit(Resource.NavigateForward())
                }
                else -> {
                }
            }
        }

    }
}