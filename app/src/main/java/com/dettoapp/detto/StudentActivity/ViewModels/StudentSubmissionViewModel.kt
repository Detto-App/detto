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
import kotlinx.coroutines.GlobalScope
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

    private lateinit var driveServiceHelper: DriveServiceHelper
    private lateinit var folder: File
    private var defaultFolderName = Utility.STUDENT.uid

    var x = MutableLiveData<String>()

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

    fun uploadFile(uri: Uri) {
        viewModelScope.launch {
            try {

                val fileName = applicationContext.contentResolver.getFileName(uri)
                val fileMIMEType = getMimeType(uri) ?: "text/plain"
                val file = java.io.File(applicationContext.cacheDir, fileName)

                copyFileToCache(uri, file)

                val mediaContent = InputStreamContent(
                    fileMIMEType,
                    BufferedInputStream(FileInputStream(file))
                )
                mediaContent.length = file.length()

                val progressListener = CustomProgressListener()
                listenForProgressUpdates(progressListener)

                val fileID = driveServiceHelper.uploadFile(
                    folder.id,
                    fileName,
                    fileMIMEType,
                    mediaContent,
                    progressListener
                ).await()


                x.postValue(fileID)

                Log.d("SSSS", "FID 2" + fileID)

            } catch (e: Exception) {
                Log.d("SSSS", "err " + e.message)
            }
        }

    }

    private fun copyFileToCache(uri: Uri, file: java.io.File) {
        val parcelFileDescriptor =
            applicationContext.contentResolver.openFileDescriptor(uri, "r", null)
                ?: throw Exception("Unable to Get Storage Access")

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)
    }

    private fun listenForProgressUpdates(progressListener: CustomProgressListener) =
        viewModelScope.launch {
            progressListener.x.collect {
                when (it) {
                    is Resource.Success -> Log.d("SSSS ", "" + it.data!!)
                    else -> {
                    }
                }
            }
        }

    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }

    private fun getMimeType(uri: Uri): String? {
        return if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            val cr: ContentResolver = applicationContext.contentResolver
            cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(fileExtension.toLowerCase(Locale.ROOT))
        }
    }

    override fun onCleared() {
        GlobalScope.launch {
            applicationContext.cacheDir.deleteRecursively()
        }
        super.onCleared()
    }
}

//                val parcelFileDescriptor =
//                    applicationContext.contentResolver.openFileDescriptor(uri, "r", null)
//                        ?: throw Exception("Unable to Get Storage Access")


//                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
//                val outputStream = FileOutputStream(file)
//
//                inputStream.copyTo(outputStream)

//                val x : FileUtils = FileUtils(context)
//                Log.d("SSSS", "" + uri.toString())
//
//                val data = File(DocumentFileCompat.fromUri(this@DummyActivity, uri)?.absolutePath)
//                Log.d("SSSS", "" + data.length())
//                Log.d("SSSS", "Path " + getPath(uri))
//            withContext(Dispatchers.Main)
//            {
//                progressListener.x.observe(this@DummyActivity,{
//                    when(it)
//                    {
//                        is Resource.Success -> Log.d("SSSS","Uploading "+it.data)
//                        is Resource.NavigateForward -> {
//                            Log.d("SSSS","Uploading Finished in main")
//                            this@DummyActivity.cacheDir.deleteRecursively()
//                        }
//
//                        else -> {}
//                    }
//                })
//            }

//mDriveServiceHelper.saveFile(mDriveServiceHelper.createFile().await(),"IDKIDK",mediaContent,progressListener)


//                val x = mDriveServiceHelper.createFile().await()
//
//                Log.d("SSSS","File id" +x)
//
//                //val data = mDriveServiceHelper.openFileUsingStorageAccessFramework(contentResolver,uri).await()
//
//                //Log.d("SSSS","Data present" +data.second)
//
//               mDriveServiceHelper.saveFile(x,"FILE 2","",uri)


//                driveServiceHelper.saveFile(
//                    driveServiceHelper.createFile(folder.id,getMimeType(uri)!!).await(),
//                    applicationContext.contentResolver.getFileName(uri),
//                    mediaContent,
//                    progressListener
//                )

//                Log.d("SSSS", "data2" + mediaContent.length)
//                Log.d("SSSS", "data3" + file.name)
//                Log.d("SSSS", "data4" + getMimeType(uri))
