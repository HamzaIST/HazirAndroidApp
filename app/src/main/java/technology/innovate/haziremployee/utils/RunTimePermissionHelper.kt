package technology.innovate.haziremployee.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class RunTimePermissionHelper(
    private var context: Context,
    private var fragment: Fragment?,
    private var permissionListener: PermissionListener,
    private var permissionList: Array<String>
) {
    private val requestMultiplePermissionsForFragment =
        fragment?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            permissions.entries.forEach {
                if (!it.value) {
                    permissionListener?.isPermissionGranted(false)
                    return@registerForActivityResult
                }
            }
            permissionListener?.isPermissionGranted(true)
        }

    fun requestForPermissions() {
        if (!isRuntimePermissionGranted()) {
            if (fragment != null) {
                requestMultiplePermissionsForFragment?.launch(permissionList)
            }


        } else {
            permissionListener.isPermissionGranted(true)

        }
    }


    fun isRuntimePermissionGranted(): Boolean {
        var result: Boolean
        for (permission in permissionList!!) {
            result = context.let {
                ContextCompat.checkSelfPermission(it, permission)
            } == PackageManager.PERMISSION_GRANTED
            if (!result) return false
        }
        return true
    }


    private var ResultLauncher =
        fragment?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (isRuntimePermissionGranted()) {
                permissionListener?.isPermissionGranted(true)

            } else
                permissionListener?.isPermissionGranted(false)


        }

    fun createAlertForPermission(msg: String) {
        val alertDialog = context?.let {
            AlertDialog.Builder(it).setTitle("Info").setCancelable(false)
                .setMessage(msg).setNegativeButton("No"
                ) { dialog12: DialogInterface, which: Int -> dialog12.dismiss() }.setPositiveButton("Go to Permissions"
                ) { dialog1: DialogInterface?, which: Int ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${context?.packageName}"))
                    context?.let { ResultLauncher?.launch(intent) } }.create()
        }
        alertDialog?.show()
    }
}



interface PermissionListener {
    fun shouldShowRationaleInfo()
    fun isPermissionGranted(isGranted: Boolean)
}