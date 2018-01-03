package com.inexture.kotlinex

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.inexture.kotlinex.permissions.PermissionUtils
import com.inexture.kotlinex.permissions.PermissionsResult
import com.livinglifetechway.k4kotlin.logD

class MiddleActivity : AppCompatActivity() {

    val REQUEST_CODE = 50
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_middle)

        val intent = this.intent.extras

        val mIntent: Intent = intent.get("myIntent") as Intent

        startActivityForResult(mIntent, REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            PermissionUtils.resultInstance?.complete(data!!)
//            PermissionUtils.resultCode.complete(resultCode)
            finish()
        }
    }
}
