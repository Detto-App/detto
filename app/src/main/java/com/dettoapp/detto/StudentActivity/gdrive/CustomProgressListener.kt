package com.dettoapp.detto.StudentActivity.gdrive

import com.dettoapp.detto.UtilityClasses.Resource
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener
import kotlinx.coroutines.flow.MutableStateFlow

class CustomProgressListener :
    MediaHttpUploaderProgressListener {

    val progressUpdates = MutableStateFlow<Resource<String>>(Resource.Success(data = "0"))

    override fun progressChanged(uploader: MediaHttpUploader?) {
        when (uploader?.uploadState) {
            MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS -> {
                progressUpdates.value = Resource.Success(data = uploader.progress.toString())
            }
            MediaHttpUploader.UploadState.MEDIA_COMPLETE -> {
                progressUpdates.value = Resource.NavigateForward()
            }
            else -> {
            }
        }
    }
}