package com.dettoapp.detto.StudentActivity.gdrive

import com.dettoapp.detto.UtilityClasses.Resource
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CustomProgressListener :
    MediaHttpUploaderProgressListener {

    val x = MutableStateFlow<Resource<String>>(Resource.Success(data = "0"))

    override fun progressChanged(uploader: MediaHttpUploader?) {
        GlobalScope.launch {
            when (uploader?.uploadState) {
                MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS -> {
                    x.emit(Resource.Success(data = uploader.progress.toString()))
                }
                MediaHttpUploader.UploadState.MEDIA_COMPLETE -> {
                    x.emit(Resource.NavigateForward())
                }
                else -> {
                }
            }
        }

    }
}