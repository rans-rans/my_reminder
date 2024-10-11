package com.rans_innovations.remind

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHandler(private val activity: Activity) {

    companion object {
        const val PERMISSION_REQUEST_CODE = 100 // Choose any value you prefer
    }

    fun requestPermissions() {
        val permissionsToRequest = arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
        val permissionsNeeded = ArrayList<String>()
        for (permission in permissionsToRequest) {
            if (ContextCompat.checkSelfPermission(
                    activity, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNeeded.add(permission)
            }
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity, permissionsNeeded.toTypedArray(), PERMISSION_REQUEST_CODE
            )
        }

    }

}