@file:Suppress("unused")

package com.dettoapp.detto.UtilityClasses

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun FragmentActivity.requestPermission(
        permission: String,
        granted: () -> Unit = {},
        denied: (permission: String) -> Unit = {},
        explained: (permission: String) -> Unit = {}
): ReadOnlyProperty<FragmentActivity, EasyPermission> {
    return ActivityDelegateSingle(permission, granted, denied, explained)
}


@SuppressLint("NewApi")
fun FragmentActivity.requestMultiplePermission(
        vararg permissions: String,
        granted: () -> Unit = {},
        denied: (permissions: List<String>) -> Unit = {},
        explained: (permissions: List<String>) -> Unit = {}
): ReadOnlyProperty<FragmentActivity, EasyPermission> {
    return ActivityDelegateMultiple(permissions = permissions.asList(), granted = granted, deniedMultiple = denied, explainedMultiple = explained)
}

fun Fragment.requestPermission(
        permission: String,
        granted: () -> Unit = {},
        denied: (permission: String) -> Unit = {},
        explained: (permission: String) -> Unit = {}
): ReadOnlyProperty<Fragment, EasyPermission> {
    return EasyPermission(this, permission = permission, granted = granted, denied = denied, explained = explained)
}

@SuppressLint("NewApi")
fun Fragment.requestMultiplePermission(
        vararg permissions: String,
        granted: () -> Unit = {},
        denied: (permissions: List<String>) -> Unit = {},
        explained: (permissions: List<String>) -> Unit = {}
): EasyPermission {
    return EasyPermission(this, permissions = permissions.asList(), granted = granted, deniedMultiple = denied, explainedMultiple = explained)
}

class ActivityDelegateSingle(private val permission: String,
                             private val granted: () -> Unit,
                             private val denied: (permission: String) -> Unit,
                             private val explained: (permission: String) -> Unit
) : ReadOnlyProperty<FragmentActivity, EasyPermission> {
    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): EasyPermission {
        return EasyPermission(activity = thisRef, permission = permission, granted = granted, denied = denied, explained = explained)
    }
}

class ActivityDelegateMultiple(
        private val permissions: List<String>,
        private val granted: () -> Unit,
        private val deniedMultiple: (permissions: List<String>) -> Unit,
        private val explainedMultiple: (permissions: List<String>) -> Unit
) : ReadOnlyProperty<FragmentActivity, EasyPermission> {
    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): EasyPermission {
        return EasyPermission(activity = thisRef, permissions = permissions, granted = granted, deniedMultiple = deniedMultiple, explainedMultiple = explainedMultiple)
    }
}


class EasyPermission(fragment: Fragment? = null,
                     activity: FragmentActivity? = null,
                     private val permission: String? = null,
                     private val permissions: List<String>? = null,
                     private val granted: (() -> Unit)? = null,
                     private val denied: ((permission: String) -> Unit)? = null,
                     private val explained: ((permission: String) -> Unit)? = null,
                     private val deniedMultiple: ((permissions: List<String>) -> Unit)? = null,
                     private val explainedMultiple: ((permissions: List<String>) -> Unit)? = null
) : ReadOnlyProperty<Any, EasyPermission> {

    private var permissionResult: ActivityResultLauncher<String>? = null
    private var permissionsResult: ActivityResultLauncher<Array<String>>? = null
    private var context: Context? = null

    init {
        activity?.let {
            context = it.applicationContext
            initialise(fragmentActivity = it)
        } ?: fragment?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                Log.d("DDSS", "OnCreate")
                context = fragment.requireContext().applicationContext
                fragment.apply {
                    initialise(fragment = this)
                }
            }

            override fun onDestroy(owner: LifecycleOwner) {
                permissionResult = null
                context = null
            }
        })
    }

    private fun initialise(fragmentActivity: FragmentActivity? = null, fragment: Fragment? = null) {
        permission?.let {
            permissionResult = initialiseSinglePermission(fragmentActivity, fragment)
        } ?: permissions?.let {
            permissionsResult = initialiseMultiplePermission(fragmentActivity, fragment)
        }
    }


    @SuppressLint("NewApi")
    private fun initialiseSinglePermission(fragmentOrActivity: FragmentActivity? = null, fragment: Fragment? = null): ActivityResultLauncher<String> {
        val fragActivityCallback = fragmentOrActivity?.let { getSinglePermissionCallBack(it) }
                ?: getSinglePermissionCallBack(fragment!!.requireActivity())

        return fragment?.registerForActivityResult(ActivityResultContracts.RequestPermission(), fragActivityCallback)
                ?: fragmentOrActivity!!.registerForActivityResult(ActivityResultContracts.RequestPermission(), fragActivityCallback)
    }

    private fun initialiseMultiplePermission(fragmentOrActivity: FragmentActivity? = null, fragment: Fragment? = null): ActivityResultLauncher<Array<String>> {
        val fragActivityCallback = fragmentOrActivity?.let { getMultiplePermissionCallBack(it) }
                ?: getMultiplePermissionCallBack(fragment!!.requireActivity())

        return fragment?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), fragActivityCallback)
                ?: fragmentOrActivity!!.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), fragActivityCallback)
    }


    @SuppressLint("NewApi")
    fun getSinglePermissionCallBack(fragmentOrActivity: FragmentActivity): ActivityResultCallback<Boolean> {
        return ActivityResultCallback { isGranted: Boolean ->
            when {
                isGranted -> granted!!()
                fragmentOrActivity.shouldShowRequestPermissionRationale(permission!!) -> denied!!(permission)
                else -> explained!!(permission)
            }
        }
    }

    @SuppressLint("NewApi")
    fun getMultiplePermissionCallBack(fragmentOrActivity: FragmentActivity): ActivityResultCallback<MutableMap<String, Boolean>> {
        return ActivityResultCallback { result ->
            val deniedList = result.filter { !it.value }.map { it.key }
            when {
                deniedList.isNotEmpty() -> {

                    val map = deniedList.groupBy { permission ->
                        if (fragmentOrActivity.shouldShowRequestPermissionRationale(permission)) "DENIED" else "EXPLAINED"
                    }

                    map["DENIED"]?.let { deniedMultiple!!(it) }

                    map["EXPLAINED"]?.let { explainedMultiple!!(it) }
                }
                else -> granted!!()
            }
        }
    }


    fun launch() {
        permissionResult?.launch(permission!!) ?: permissionsResult?.launch(permissions!!.toTypedArray())
    }

    fun check() {
        permissionResult?.let {
            checkSingle(false)
        } ?: permissionsResult?.let {
            checkMultiple(false)
        }
    }

    fun checkAndLaunch() {
        permissionResult?.let {
            checkSingle(true)
        } ?: permissionsResult?.let {
            checkMultiple(true)
        }
    }

    private fun checkSingle(isLaunching: Boolean) {
        if (selfCheckPermission(permission!!)) {
            granted!!()
        } else {
            if (!isLaunching)
                denied!!(permission)
            else
                permissionResult?.launch(permission)
        }
    }

    private fun checkMultiple(isLaunching: Boolean) {
        val deniedList = permissions!!.filter {
            !selfCheckPermission(it)
        }.map { it }

        if (deniedList.isEmpty())
            granted!!()
        else {
            if (!isLaunching)
                deniedMultiple!!(deniedList)
            else
                permissionsResult?.launch(permissions.toTypedArray())
        }

    }

    private fun selfCheckPermission(permission: String) = ContextCompat.checkSelfPermission(
            context!!,
            permission
    ) == PackageManager.PERMISSION_GRANTED

    override fun getValue(thisRef: Any, property: KProperty<*>): EasyPermission {
        return this
    }
}