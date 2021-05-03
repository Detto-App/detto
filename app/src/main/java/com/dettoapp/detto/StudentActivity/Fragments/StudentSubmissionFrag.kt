package com.dettoapp.detto.StudentActivity.Fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentSubmissionViewModel
import com.dettoapp.detto.StudentActivity.gdrive.UploadGDriveWorker
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.EasyPermission
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.UtilityClasses.requestPermission
import com.dettoapp.detto.databinding.FragmentStudentSubmissionBinding

class StudentSubmissionFrag :
        BaseFragment<StudentSubmissionViewModel, FragmentStudentSubmissionBinding, StudentRepository>() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val readStoragePermissionResult: EasyPermission by requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
            granted = {
                displayView(true)
            }, denied = {
        displayView(false)
    })

    override fun getBaseOnCreate() {
        super.getBaseOnCreate()
        resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    handleIt(it)
                }
    }

    private fun handleIt(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            data?.let {
                scheduleUploadWorker(it, viewModel.gDriveToken)
            }
        }
    }

    private fun scheduleUploadWorker(uri: Uri, gDriveToken: String) {

        val data = Data.Builder()
                .putString(UploadGDriveWorker.URI_PATH, uri.toString())
                .putString(UploadGDriveWorker.GDRIVE_TOKEN, gDriveToken)
                .putString(UploadGDriveWorker.FOLDER_NAME, Utility.STUDENT.name.toUpperCase() + "-" + Utility.STUDENT.susn.toUpperCase())
                .build()

        //registerForActivityResult(ActivityResultContracts.RequestPermission())
        val uploadWorkRequest =
                OneTimeWorkRequestBuilder<UploadGDriveWorker>()
                        .setInputData(data)
                        .build()

        WorkManager
                .getInstance(requireContext().applicationContext)
                .enqueue(uploadWorkRequest)

        baseActivity.showToast("File Uploading\nCheck Notifications")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        checkPermissions()
        initialise()
    }

    private fun initialise() {
        binding.enableStoragePermission.setOnClickListener {
            readStoragePermissionResult.launch()
        }

        binding.fileChooser.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            resultLauncher.launch(intent)
        }
    }

    private fun checkPermissions() {
        readStoragePermissionResult.check()
    }

    override fun getViewModelClass(): Class<StudentSubmissionViewModel> {
        return StudentSubmissionViewModel::class.java
    }

    override fun getFragmentBinding(
            inflater: LayoutInflater,
            container: ViewGroup?
    ): FragmentStudentSubmissionBinding {
        return FragmentStudentSubmissionBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): StudentRepository {
        return StudentRepository(
                DatabaseDetto.getInstance(requireContext().applicationContext).classroomDAO,
                DatabaseDetto.getInstance(requireContext().applicationContext).projectDAO
        )
    }

    private fun displayView(isShowing: Boolean = false) {
        if (isShowing) {
            binding.enableStoragePermission.visibility = View.GONE

            binding.fileChooser.visibility = View.VISIBLE
            binding.fileName.visibility = View.VISIBLE
        } else {
            binding.enableStoragePermission.visibility = View.VISIBLE

            binding.fileChooser.visibility = View.GONE
            binding.fileName.visibility = View.GONE
        }
    }
}

//class ActivityResultLazy<R : ActivityResultLauncher<String>>(private val fragment: Fragment, private val permission: String,
//                                                             private val granted: (permission: String) -> Unit) :
//        ReadOnlyProperty<Fragment, R> {
//
//    private var permissionResult: ActivityResultLauncher<String>? = null
//
//
//    init {
//        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
//            override fun onCreate(owner: LifecycleOwner) {
//                fragment.apply {
//                    permissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()
//                    ) { isGranted: kotlin.Boolean ->
//                        if (isGranted) {
//                            granted(permission)
//                            // Permission is granted. Continue the action or workflow in your
//                            // app.
//                        } else {
//                            android.util.Log.d("DDSS", "R D 2")
//                            // Explain to the user that the feature is unavailable because the
//                            // features requires a permission that the user has denied. At the
//                            // same time, respect the user's decision. Don't link to system
//                            // settings in an effort to convince the user to change their
//                            // decision.
//                        }
//                    }
//                }
//            }
//
//            override fun onDestroy(owner: LifecycleOwner) {
//                permissionResult = null
//            }
//        })
//    }
//
//    override fun getValue(thisRef: Fragment, property: KProperty<*>): R {
//        permissionResult?.let { return (it as R) }
//
//        error("Not Working")
//    }
////    override val value: R
////        get() {
////            x.apply {
////                val x2 = registerForActivityResult(ActivityResultContracts.RequestPermission()
////                ) { isGranted: Boolean ->
////                    if (isGranted) {
////                        Log.d("DDSS", "R G 2")
////                        // Permission is granted. Continue the action or workflow in your
////                        // app.
////                    } else {
////                        Log.d("DDSS", "R D 2")
////                        // Explain to the user that the feature is unavailable because the
////                        // features requires a permission that the user has denied. At the
////                        // same time, respect the user's decision. Don't link to system
////                        // settings in an effort to convince the user to change their
////                        // decision.
////                    }
////                }
////                return (x2 as R)
////            }
////        }
////
////    override fun isInitialized(): Boolean {
////        return false
////    }
//}
//
//inline fun <reified R : ActivityResultLauncher<String>> Fragment.requestPermission(
//        permission: String,
//        noinline granted: (permission: String) -> Unit = {},
//        crossinline denied: (permission: String) -> Unit = {},
//        crossinline explained: (permission: String) -> Unit = {}
//
//): ReadOnlyProperty<Fragment, R> = ActivityResultLazy(this, permission, granted)


//        val x = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
//            when {
//                result -> granted.invoke(permission)
//                shouldShowRequestPermissionRationale(permission) -> denied.invoke(permission)
//                else -> {
//                    x.launch(permission)
//                    //denied.invoke(permission)
//                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_MEDIA_LOCATION
//                            ) == PackageManager.PERMISSION_GRANTED) {
//                        granted.invoke(permission)
//                    }
//                    else
//                    {
//                        Log.d("DDSS","Heyy")
//                    }
//                }
//            }
//        }
//
//        return ActivityResultLazy(x)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//
//    }

//    private lateinit var resultLauncher2: ActivityResultLauncher<String>
//
//    val x2: StudentSubmissionViewModel by activityViewModels()
//    val x3: FragmentStudentSubmissionBinding by viewBinding()
//
//    val x: ActivityResultLauncher<String> by requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, {
//        Log.d("DDSS", "hello g")
//    }, {
//        Log.d("DDSS", "hello d")
////        ActivityCompat.requestPermissions(this.requireActivity(),
////                arrayOf(it),
////                10)
//    }, {
//        Log.d("DDSS", "hello e")
//    })