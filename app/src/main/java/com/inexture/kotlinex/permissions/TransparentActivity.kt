package com.inexture.kotlinex.permissions

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.inexture.kotlinex.R
import pub.devrel.easypermissions.EasyPermissions

class TransparentActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {


    private var manager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trans_parent)

        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        checkForPermission()

    }

    private fun checkForPermission() {
        val perms = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            finish()
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "permission required",
                    1, *perms)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        val permsList: ArrayList<Boolean> = arrayListOf(true, true)
        PermissionUtils.instance?.complete(permsList)
        for (i in permissions){

        }
        finish()

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        finish()
    }
}
