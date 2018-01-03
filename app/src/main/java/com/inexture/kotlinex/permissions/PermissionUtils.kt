package com.inexture.kotlinex.permissions

import android.content.Intent
import kotlinx.coroutines.experimental.CompletableDeferred

/**
 * Created by Inexture on 12/29/2017.
 */
object PermissionUtils {
    var instance: CompletableDeferred<PermissionsResult>? = null

    var resultInstance: CompletableDeferred<Intent>? = null
    var resultCode: CompletableDeferred<Int>? = null
}