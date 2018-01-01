package com.inexture.kotlinex.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.inexture.kotlinex.R
import com.livinglifetechway.k4kotlin.logD
import pub.devrel.easypermissions.EasyPermissions

class TransparentActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {


    //    private var manager: LocationManager? = null
    private val MY_PERMISSIONS_REQUEST_CODE = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trans_parent)

        val intent = this.intent.extras
        val perms = intent.getStringArrayList("permsList")
//        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        checkPermissions(perms)

    }

    private fun checkPermissions(perms: ArrayList<String>) {
//        val perms = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA)

        val deniedPerms = arrayListOf<String>()
        val reqPerms = arrayListOf<String>()

        for (permission in perms) {
            if (ContextCompat.checkSelfPermission(this@TransparentActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@TransparentActivity, permission)) {
                    deniedPerms.add(permission)
                } else {
                    reqPerms.add(permission)
                }
            }
        }
        if (deniedPerms.size > 0) {
            ActivityCompat.requestPermissions(this@TransparentActivity, deniedPerms.toTypedArray(), MY_PERMISSIONS_REQUEST_CODE)
        }
        if (reqPerms.size > 0) {
            ActivityCompat.requestPermissions(this@TransparentActivity, reqPerms.toTypedArray(), MY_PERMISSIONS_REQUEST_CODE)
        }
    }

    @SuppressLint("NewApi")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        val permsResult = PermissionsResult(permissions, grantResults)
        val permsList: ArrayList<Boolean> = arrayListOf()
//
//        for (i in 1..permissions.size) {
//            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                val showRationale = shouldShowRequestPermissionRationale(permissions[i])
//                if (!showRationale) {
//
//                } else {
//
//                }
//            }
//        }


        if (requestCode == MY_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                for (perm in grantResults) {
                    if (perm == PackageManager.PERMISSION_GRANTED) {
                        permsList.add(true)
                    } else if (perm == PackageManager.PERMISSION_DENIED) {
                        permsList.add(false)
                    }
                }
            }
        }

        PermissionUtils.instance?.complete(permsResult)
        finish()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        val deniedPerms = perms
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        val grantedPerms = perms
        finish()
    }
}
