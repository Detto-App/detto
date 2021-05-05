package com.dettoapp.detto.StudentActivity.gdrive

import android.app.Notification
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.InputStreamContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


@Suppress("SpellCheckingInspection", "BlockingMethodInNonBlockingContext")
class UploadGDriveWorker(context: Context, parameters: WorkerParameters) :
        CoroutineWorker(context, parameters) {

    companion object {
        const val URI_PATH = "uriPath"
        const val GDRIVE_TOKEN = "gToken"
        const val FOLDER_NAME = "folder"
        const val FILE_NAME = "fileName"
        private val counter = AtomicInteger(6)
    }

    private val notificationManager: NotificationManager by lazy { applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private var driveServiceHelper: DriveServiceHelper? = null
    private lateinit var folder: File
    private var fileName: String = "Uploading"
    private lateinit var errorHandler :CoroutineExceptionHandler

    override suspend fun doWork(): Result {
        return try {
             errorHandler = CoroutineExceptionHandler { context, error ->
                when (error) {
                    is FinishedException ->
                        Result.success()
                }
            }

            val uniqueID = getID()
            beginTask(uniqueID)
            Result.success()
        } catch (e: CancellationException) {
            //Log.d("SSSS", "" + e.localizedMessage)
            driveServiceHelper?.stopGDriveService()
            Result.failure()
        } catch (e: FinishedException) {
            //Log.d("SSSS", "" + e.localizedMessage)
            Result.success()
        } catch (e: Exception) {
            //Log.d("SSSS", "" + e.localizedMessage)
            showErrorNotification()
            Result.failure()
        }
    }

    private suspend fun beginTask(uniqueID: Int) {
        setForeground(createForegroundInfo(uniqueID))
        val uri =
                inputData.getString(URI_PATH)?.toUri() ?: throw Exception("Unable to Locate File")
        val gDriveToken = inputData.getString(GDRIVE_TOKEN)
                ?: throw Exception("Could not Connect to GoogleDrive")

        val folderName =
                inputData.getString(FOLDER_NAME) ?: throw Exception("No folder Information")

        val fileName =
                inputData.getString(FILE_NAME) ?: throw Exception("No fileName Information Information")

        if (driveServiceHelper == null)
            initialiseDriveService(gDriveToken)

        getIndividualFolderID(folderName)
        uploadFile(uri, uniqueID, fileName)
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

    private fun getIndividualFolderID(defaultFolderName: String) {
        folder =
                driveServiceHelper?.createOrGetFolderReference(defaultFolderName)
                        ?: throw Exception("No Gdrive Connection")
    }

    private fun createNotification(message: String): Notification {
        val title = fileName
        val cancel = "cancel"

        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext)
                .createCancelPendingIntent(id)


        return NotificationCompat.Builder(applicationContext, Constants.PROGRESS_CHANNEL_ID)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_delete, cancel, intent)
                .build()
    }

    private fun createForegroundInfo(uniqueID: Int, initialMessage: String = "Starting"): ForegroundInfo {
        return ForegroundInfo(uniqueID, createNotification(initialMessage))
    }

    private suspend fun uploadFile(uri: Uri, uniqueID: Int, fileNameReceived: String) {
        fileName = fileNameReceived
        val fileMIMEType = getMimeType(uri) ?: "text/plain"
        val path = applicationContext.cacheDir

        val file = java.io.File(path, fileName)

        copyFileToCache(uri, file)

        val mediaContent = InputStreamContent(
                fileMIMEType,
                BufferedInputStream(FileInputStream(file))
        )
        mediaContent.length = file.length()

        val progressListener = CustomProgressListener()
        listenForProgressUpdates(progressListener, uniqueID)

        val fileID = driveServiceHelper!!.uploadFile(
                folder.id,
                fileName,
                fileMIMEType,
                mediaContent,
                progressListener
        ).await()


    }

    private fun getFileName(fileUri: Uri): String {
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

    private fun copyFileToCache(uri: Uri, file: java.io.File) {
        val parcelFileDescriptor =
                applicationContext.contentResolver.openFileDescriptor(uri, "r", null)
                        ?: throw Exception("Unable to Get Storage Access")

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)
    }

    private fun listenForProgressUpdates(progressListener: CustomProgressListener, uniqueID: Int) {
        CoroutineScope(errorHandler).launch {
            progressListener.progressUpdates.collect {
                when (it) {
                    is Resource.Success -> displayFileUpLoadProgress(it.data!!, uniqueID)
                    is Resource.NavigateForward -> {
                        throw FinishedException("Done")
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun displayMessage(message: String, uniqueID: Int) {
        notificationManager.notify(uniqueID, createNotification(message))
    }

    private fun displayFileUpLoadProgress(progressString: String, uniqueID: Int) {
        val number = progressString.toDouble() * 100
        val filterUserPrice: String = "%.2f".format(number)
        displayMessage("Uploading $filterUserPrice%", uniqueID)
    }

    private fun getID() = counter.incrementAndGet()

    inner class FinishedException(message: String) : Exception(message)

    private fun showErrorNotification(message: String = "Unable to upload file\nCheck Network Connection") {
        val notification = NotificationCompat.Builder(applicationContext, Constants.PROGRESS_CHANNEL_ID)
                .setContentTitle(fileName)
                .setTicker(fileName)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}