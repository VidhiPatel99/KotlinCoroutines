package com.inexture.kotlinex.permissions

import android.content.pm.PackageManager

/**
 * Created by Inexture on 1/1/2018.
 */
class PermissionsResult(val permissions: Array<out String>, val grantResult: IntArray) {
    private val isAllDenied: Boolean = false


    fun getAllGrantedPermission(): ArrayList<String> {
        val grantedPerms = arrayListOf<String>()
        (0 until permissions.size)
                .filter { grantResult[it] == PackageManager.PERMISSION_GRANTED }
                .mapTo(grantedPerms) { permissions[it] }
        return grantedPerms
    }

    fun getAllDeniedPermission(): ArrayList<String> {
        val deniedPerms = arrayListOf<String>()
        (0 until permissions.size)
                .filter { grantResult[it] == PackageManager.PERMISSION_DENIED }
                .mapTo(deniedPerms) { permissions[it] }
        return deniedPerms
    }

    fun getGrantedPermissionCount(): Int {
        val grantedPerms = arrayListOf<String>()
        (0 until permissions.size)
                .filter { grantResult[it] == PackageManager.PERMISSION_GRANTED }
                .mapTo(grantedPerms) { permissions[it] }
        return grantedPerms.size
    }

    fun getDeniedPermissionCount(): Int {
        val deniedPerms = arrayListOf<String>()
        (0 until permissions.size)
                .filter { grantResult[it] == PackageManager.PERMISSION_DENIED }
                .mapTo(deniedPerms) { permissions[it] }
        return deniedPerms.size
    }

    fun isAllPermsGranted(): Boolean {
        val isAllGranted: Boolean
        val grantedPerms = arrayListOf<String>()
        (0 until permissions.size)
                .filter { grantResult[it] == PackageManager.PERMISSION_GRANTED }
                .mapTo(grantedPerms) { permissions[it] }
        isAllGranted = grantedPerms.size == permissions.size

        return isAllGranted
    }

    fun isAllPermsDenied(): Boolean {
        val isAllDenied: Boolean
        val deniedPerms = arrayListOf<String>()
        (0 until permissions.size)
                .filter { grantResult[it] == PackageManager.PERMISSION_DENIED }
                .mapTo(deniedPerms) { permissions[it] }
        isAllDenied = deniedPerms.size == permissions.size

        return isAllDenied
    }
}