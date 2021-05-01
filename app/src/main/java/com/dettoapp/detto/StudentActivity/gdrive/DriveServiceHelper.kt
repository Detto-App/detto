package com.dettoapp.detto.StudentActivity.gdrive

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.dettoapp.detto.UtilityClasses.Utility
import com.google.android.gms.drive.DriveFolder
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.InputStreamContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import com.google.api.services.drive.model.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.io.*
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executor


/**
 * UnComment as required and use
 * var TYPE_AUDIO = "application/vnd.google-apps.audio"
 * var TYPE_GOOGLE_DOCS = "application/vnd.google-apps.document"
 * var TYPE_GOOGLE_DRAWING = "application/vnd.google-apps.drawing"
 * var TYPE_GOOGLE_DRIVE_FILE = "application/vnd.google-apps.file"
 * var TYPE_GOOGLE_FORMS = "application/vnd.google-apps.form"
 * var TYPE_GOOGLE_FUSION_TABLES = "application/vnd.google-apps.fusiontable"
 * var TYPE_GOOGLE_MY_MAPS = "application/vnd.google-apps.map"
 * var TYPE_PHOTO = "application/vnd.google-apps.photo"
 * var TYPE_GOOGLE_SLIDES = "application/vnd.google-apps.presentation"
 * var TYPE_GOOGLE_APPS_SCRIPTS = "application/vnd.google-apps.script"
 * var TYPE_GOOGLE_SITES = "application/vnd.google-apps.site"
 * var TYPE_GOOGLE_SHEETS = "application/vnd.google-apps.spreadsheet"
 * var TYPE_UNKNOWN = "application/vnd.google-apps.unknown"
 * var TYPE_VIDEO = "application/vnd.google-apps.video"
 * var TYPE_3_RD_PARTY_SHORTCUT = "application/vnd.google-apps.drive-sdk"
 */


@Suppress("PrivatePropertyName")
class DriveServiceHelper(driveService: Drive) {

    private val TYPE_GOOGLE_DRIVE_FOLDER = DriveFolder.MIME_TYPE
    private val mExecutor: Executor = Dispatchers.IO.asExecutor()
    private val mDriveService: Drive = driveService


    @Throws(IOException::class)
    fun listDriveImageFiles(): List<File?>? {
        var i = 0;
        var result: FileList
        var pageToken: String? = null
        do {
            i++
            result = mDriveService.files()
                .list()
                .setQ(
                    " mimeType = 'application/vnd.google-apps.folder' "
                            + " and 'root' in parents"
                )
//                   .setQ("mimeType='image/png' or mimeType='text/pdf'")
                //setQ("'" + "root" + "' in parents")
                /*.setQ("mimeType='image/png' or mimeType='text/plain'")This si to list both image and text files. Mind the type of image(png or jpeg).setQ("mimeType='image/png' or mimeType='text/plain'") */
                .setSpaces("drive")
                .setFields("nextPageToken, files(id, name)")
                .setPageToken(pageToken)
                .execute()
            Log.d("SSSS", "hhdd")
            pageToken = result.nextPageToken
        } while (pageToken != null)
        return result.files
    }

    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */
    fun createFile(folderID: String = "root", mimeType: String = "text/plain"): Task<String> {
        return Tasks.call(mExecutor, Callable {
            val metadata: File = File()
                .setParents(Collections.singletonList(folderID))
                .setMimeType(mimeType)
                .setName("Untitled file")


            val googleFile: File = mDriveService.files().create(metadata).execute()
                ?: throw IOException("Null result when requesting file creation.")
            googleFile.getId()
        })
    }

    /**
     * Opens the file identified by `fileId` and returns a [Pair] of its name and
     * contents.
     */
    fun readFile(fileId: String?): Task<Pair<String, String>> {
        return Tasks.call(mExecutor, Callable {

            // Retrieve the metadata as a File object.
            val metadata: File = mDriveService.files().get(fileId).execute()
            val name: String = metadata.getName()
            mDriveService.files().get(fileId).executeMediaAsInputStream().use { `is` ->
                BufferedReader(InputStreamReader(`is`)).use { reader ->
                    val stringBuilder = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line)
                    }
                    val contents = stringBuilder.toString()
                    return@use Pair(name, contents)
                }
            }
        })
    }

    /**
     * Updates the file identified by `fileId` with the given `name` and `content`.
     */
    fun saveFile(fileId: String?, name: String?, content: String?, uri: Uri): Task<Void> {
        return Tasks.call(mExecutor, Callable {

            Log.d("SSSS", "Hello")
            // Create a File containing any metadata changes.
            val metadata: File = File().setName(name)

            // Convert content to an AbstractInputStreamContent instance.
            val contentStream = ByteArrayContent.fromString("application/pdf", content)

//            val request = mDriveService.files().u
//            request.mediaHttpDownloader.progressListener = CustomProgressListener()
            //request.execute()


            val mediaFile = java.io.File(uri.toString())


            val mediaContent = InputStreamContent(
                "application/pdf",
                BufferedInputStream(FileInputStream(mediaFile))
            )

            mediaContent.length = mediaFile.length()

            Log.d("SSSS", "File Path " + uri.path)
            Log.d("SSSS", "File Size " + mediaContent.length)


            // Update the metadata and contents.

            val type = mDriveService.files().update(fileId, metadata, mediaContent)
            //type.mediaHttpUploader.progressListener = CustomProgressListener(v)

            //val request: Drive.Files.Insert = drive.files().insert(fileMetadata, mediaContent)

            //type.execute()
            null
        })
    }


//    fun saveFile(
//        fileId: String?,
//        name: String?,
//        mediaContent: InputStreamContent,
//        uploader: CustomProgressListener,
//    ): Task<Void> {
//        return Tasks.call(mExecutor, Callable {
//            // Update the metadata and contents.
////            val type = mDriveService.files().update(fileId, File().setName("IDKIDK"), mediaContent)
////            val up = type.mediaHttpUploader
//
//
////            val metadata: File = File()
////                .setParents(Collections.singletonList(Utility.STUDENT.uid))
////                .setMimeType("image/jpg")
////                .setName("Untitled file")
//
//
////            val permission: Permission = Permission()
////                .setType("anyone")
////                .setRole("reader")
////
////            val x = mDriveService.Permissions().create(fileId, permission).execute()
////
////            val file: File = mDriveService.files().get(fileId).setFields("webViewLink").execute()
////
////            Log.d("FFGG", file.webViewLink)
//
//
//            //val type = mDriveService.files().create(metadata,mediaContent)
//            val type = mDriveService.files().update(fileId, File().setName(name), mediaContent)
//            type.setFields("id")
//            val up = type.mediaHttpUploader
//
//            up.setDirectUploadEnabled(false)
//            up.setChunkSize(MediaHttpUploader.MINIMUM_CHUNK_SIZE)
//
//
//            type.mediaHttpUploader.progressListener = uploader
//
//            //val request: Drive.Files.Insert = drive.files().insert(fileMetadata, mediaContent)
//
//            type.execute()
//            null
//        })
//    }



    /**
     * Returns a [FileList] containing all the visible files in the user's My Drive.
     *
     *
     * The returned list will only contain files visible to this app, i.e. those which were
     * created by this app. To perform operations on files not created by the app, the project must
     * request Drive Full Scope in the [Google
 * Developer's Console](https://play.google.com/apps/publish) and be submitted to Google for verification.
     */
    fun queryFiles(): Task<FileList> {
        return Tasks.call(mExecutor,
            Callable {
                mDriveService.files().list().setSpaces("drive").execute()
            })
    }

    /**
     * Returns an [Intent] for opening the Storage Access Framework file picker.
     */
    fun createFilePickerIntent(): Intent {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        //intent.type = "text/plain"
        return intent
    }

    /**
     * Opens the file at the `uri` returned by a Storage Access Framework [Intent]
     * created by [.createFilePickerIntent] using the given `contentResolver`.
     */
    fun openFileUsingStorageAccessFramework(
        contentResolver: ContentResolver, uri: Uri?
    ): Task<Pair<String, String>> {
        return Tasks.call(mExecutor, Callable {

            // Retrieve the document's display name from its metadata.
            var name: String
            contentResolver.query(uri!!, null, null, null, null).use { cursor ->
                name = if (cursor != null && cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.getString(nameIndex)
                } else {
                    throw IOException("Empty cursor returned for file.")
                }
            }

            // Read the document's contents as a String.
            var content: String
            contentResolver.openInputStream(uri!!).use { `is` ->
                BufferedReader(InputStreamReader(`is`)).use { reader ->
                    val stringBuilder = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line)
                    }
                    content = stringBuilder.toString()
                }
            }
            Pair(name, content)
            //Pair.create(name, content)
        })
    }

    fun createOrGetFolderReference(folderName: String) =
        getFolderID(folderName) ?: createFolder(folderName)

    private fun getFolderID(folderName: String): File? {
        var pageToken: String? = null
        var file: File? = null
        do {
            val result: FileList = mDriveService.files().list()
                .setQ("mimeType='$TYPE_GOOGLE_DRIVE_FOLDER' and trashed=false and name='$folderName'")
                .setSpaces("drive")
                .setFields("nextPageToken, files(id, name)")
                .setPageToken(pageToken)
                .execute()

            if (result.files != null && result.files.size == 1)
                file = result.files[0]
            pageToken = result.nextPageToken
        } while (pageToken != null)

        return file
    }

    private fun createFolder(folderName: String, isShareable: Boolean = true): File {
        val fileMetadata = File()
        fileMetadata.name = folderName
        fileMetadata.mimeType = "application/vnd.google-apps.folder"


        val file: File = mDriveService.files().create(fileMetadata)
            .setFields("id")
            .execute()

        if (isShareable) {
            makeFolderShareable(file.id)
        }

        return file
    }

    private fun makeFolderShareable(folderID: String) {
        val permission: Permission = Permission()
            .setType("anyone")
            .setRole("reader")

        mDriveService.Permissions().create(folderID, permission).execute()
    }

    fun uploadFile(folderID: String,name: String,mimeType: String,mediaContent: InputStreamContent,customProgressListener: CustomProgressListener): Task<String> = Tasks.call(mExecutor){

        val metadata: File = File()
            .setParents(Collections.singletonList(folderID))
            .setMimeType(mimeType)
            .setName(name)

        val createRequest = mDriveService.files().create(metadata,mediaContent)
        createRequest.fields ="id, webViewLink"

        createRequest.mediaHttpUploader.apply {
            isDirectUploadEnabled = false
            chunkSize = MediaHttpUploader.MINIMUM_CHUNK_SIZE
            progressListener  = customProgressListener
        }
        //            val up = type.mediaHttpUploader

        //            up.setDirectUploadEnabled(false)
//            up.setChunkSize(MediaHttpUploader.MINIMUM_CHUNK_SIZE)
//
//
//            type.mediaHttpUploader.progressListener = uploader

        val output = createRequest.execute()

        Log.d("SSSS", "File ID "+output.webViewLink)
        return@call output.webViewLink
    }

}