package com.dettoapp.detto.StudentActivity.gdrive

import androidx.lifecycle.MutableLiveData
import com.dettoapp.detto.UtilityClasses.Resource
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener

class CustomProgressListener : MediaHttpUploaderProgressListener {

    val x = MutableLiveData<Resource<String>>()

    override fun progressChanged(uploader: MediaHttpUploader?) {
        when (uploader?.uploadState) {
            MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS -> {
                x.postValue(Resource.Success(data = uploader.progress.toString()))
            }
            MediaHttpUploader.UploadState.MEDIA_COMPLETE ->{  x.postValue(Resource.NavigateForward())}
            else -> { }

        }
    }
}