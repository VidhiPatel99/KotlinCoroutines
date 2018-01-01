package com.inexture.kotlinex.permissions

import kotlinx.coroutines.experimental.CompletableDeferred

/**
 * Created by Inexture on 12/29/2017.
 */
object PermissionUtils {
    var instance: CompletableDeferred<PermissionsResult>? = null

    var grantedPermission: ArrayList<String>? = null
    var deniedPermission: ArrayList<String>? = null

    fun onPermissionRequested(grantedPermission: ArrayList<String>, deniedPermission: ArrayList<String>) {
        this.grantedPermission?.addAll(grantedPermission)
        this.deniedPermission?.addAll(deniedPermission)
    }

}