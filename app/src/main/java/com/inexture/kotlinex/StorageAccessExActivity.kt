package com.inexture.kotlinex

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import com.bumptech.glide.Glide
import com.inexture.kotlinex.databinding.ActivityStorageAccessExBinding
import android.preference.PreferenceManager
import android.widget.EditText
import android.widget.LinearLayout
import com.inexture.kotlinex.permissions.PermissionsResult
import com.livinglifetechway.k4kotlin.*
import java.io.*
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async


class StorageAccessExActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityStorageAccessExBinding
    private val READ_REQUEST_CODE = 11
    private val WRITE_REQUEST_CODE = 12
    private val DELETE_REQUEST_CODE = 13
    private val EDIT_REQUEST_CODE = 14
    private val STORE_REQUEST_CODE = 15
    private val OPEN_REQUEST_CODE = 16
    private val OPEN_VIDEO_REQUEST_CODE = 17
    private val OPEN_IMAGE_REQUEST_CODE = 18
    private val OPEN_AUDIO_REQUEST_CODE = 19
    lateinit var myIntent: CompletableDeferred<PermissionsResult>
    lateinit var permsResult: PermissionsResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = setBindingView(R.layout.activity_storage_access_ex)


        //open text file and set its text in textView
        mBinding.btnChoose.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/plain"
//            startActivityForResult(intent, READ_REQUEST_CODE)
            async(UI) {
                val resultIntent = startNewActivity(intent)
                val result = resultIntent?.await()
                if (result != null) {
                    val uri = result.data
                    uri.logD("textUri")
                    //get inputStream
                    val inputStream = contentResolver.openInputStream(uri)
                    val reader = BufferedReader(InputStreamReader(
                            inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String? = ""
                    while (true) {
                        line = reader.readLine()
                        if (line == null)
                            break
                        stringBuilder.append(line)
                    }
                    mBinding.tvInputStream.text = stringBuilder
                }
            }
        }

        //create "myNewFile" named(default) text file, can edit file name
        mBinding.btnCreate.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TITLE, "myNewFile")
//            startActivityForResult(intent, WRITE_REQUEST_CODE)

            async(UI) {
                val resultIntent = startNewActivity(intent)
                val result = resultIntent?.await()
                if (result != null) {
                    toastNow("File created successfully")
                }
            }
        }

        //delete text file which you have selected
        mBinding.btnDelete.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
//            startActivityForResult(intent, DELETE_REQUEST_CODE)

            async(UI) {
                val resultIntent = startNewActivity(intent)
                val result = resultIntent?.await()
                if (result != null) {
                    DocumentsContract.deleteDocument(contentResolver, result.data)
                    toastNow("File deleted successfully")

                }
            }
        }

        //edit text file by writing anything in editor dialog
        mBinding.btnEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/plain"
//            startActivityForResult(intent, EDIT_REQUEST_CODE)
            async(UI) {
                val resultIntent = startNewActivity(intent)
                val result = resultIntent?.await()
                if (result != null) {
                    try {
                        val pfd = applicationContext.contentResolver.openFileDescriptor(result.data, "w")
                        val fileOutputStream = FileOutputStream(pfd.fileDescriptor)
                        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@StorageAccessExActivity)
                        alertDialog.setTitle("Test")
                        alertDialog.setMessage("Enter something")

                        val input = EditText(this@StorageAccessExActivity)
                        val lp = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT)
                        input.layoutParams = lp
                        alertDialog.setView(input)

                        alertDialog.setPositiveButton("Ok", { dialog: DialogInterface?, which: Int ->
                            fileOutputStream.write((input.value +
                                    System.currentTimeMillis() + "\n").toByteArray())
                            fileOutputStream.close()
                            pfd.close()
                        })

                        alertDialog.setNegativeButton("Cancel", { dialog: DialogInterface?, which: Int ->
                            dialog?.cancel()
                        })

                        alertDialog.show()


                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

        }

        //persist permission example (store uri in preference and can open by clicking "OPEN FILE" button)
        mBinding.btnStoreUri.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION  //add flag for grant the access of uri even after device restarted
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/plain"
//            startActivityForResult(intent, STORE_REQUEST_CODE)

            async(UI) {
                val resultIntent = startNewActivity(intent)
                val result = resultIntent?.await()
                if (result != null) {
                    PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putString("URI", result.data.toString()).apply()

                    //add flag for persist permission
                    val takeFlags = result.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    applicationContext.contentResolver.takePersistableUriPermission(result.data, takeFlags)
                }
            }
        }

        //open file which has uri stored in preference(can also open after restart the device)
        //read the file line by line
        //append string from new line and set it in textView
        mBinding.btnPermission.setOnClickListener {
            val uri = PreferenceManager.getDefaultSharedPreferences(applicationContext).getString("URI", "")
            if (uri.isNotBlank()) {
                val inputStream = contentResolver.openInputStream(Uri.parse(uri))
                val reader = BufferedReader(InputStreamReader(
                        inputStream))
                val stringBuilder = StringBuilder()
                var line: String? = ""
                while (true) {
                    line = reader.readLine()
                    if (line == null)
                        break
                    stringBuilder.append(line)
                }
                mBinding.tvInputStream.text = stringBuilder
            }
        }

        //show video file and play it in videoView
        mBinding.btnVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "video/*"
//            startActivityForResult(intent, OPEN_VIDEO_REQUEST_CODE)

            async(UI) {
                val resultIntent = startNewActivity(intent)
                val result = resultIntent?.await()
                if (result != null) {
                    mBinding.videoView.setVideoURI(result.data)
                    mBinding.videoView.setOnPreparedListener { mp ->
                        mp?.isLooping = true
                        mBinding.videoView.start()
                    }
                }
            }
        }

        //show image file and set it in imageView
        mBinding.btnImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
//            startActivityForResult(intent, OPEN_IMAGE_REQUEST_CODE)

            async(UI) {
                val resultIntent = startNewActivity(intent)
                val result = resultIntent?.await()
                if (result != null) {
                    val uri = result.data
                    uri.logD("imageUri")
                    Glide.with(this@StorageAccessExActivity)
                            .load(uri)
                            .into(mBinding.ivShow)
                }
            }
        }

        //show audio file and play it
        mBinding.btnAudio.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "audio/*"
//            startActivityForResult(intent, OPEN_AUDIO_REQUEST_CODE)

            async(UI) {
                val resultIntent = startNewActivity(intent)
                val result = resultIntent?.await()
                if (result != null) {
                    mBinding.videoView.setVideoURI(result.data)
                    mBinding.videoView.setOnPreparedListener { mp ->
                        mp?.isLooping = true
                        mBinding.videoView.start()
                    }
                }
            }
        }

        val perms = arrayListOf(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA)



        mBinding.btnTest.setOnClickListener {
            async(UI) {
                myIntent = checkForPermissions(perms)
                permsResult = myIntent.await()
                permsResult.getAllGrantedPermission().logD("granted")
                permsResult.getAllDeniedPermission().logD("denied")
                permsResult.getGrantedPermissionCount().logD("grantedCount")
                permsResult.getDeniedPermissionCount().logD("deniedCount")
                permsResult.isAllPermsGranted().logD("isAllGranted")
                permsResult.isAllPermsDenied().logD("isAllDenied")
                permsResult.isPermGranted(Manifest.permission.READ_CONTACTS).logD("isGranted")
                permsResult.isPermDenied(Manifest.permission.ACCESS_FINE_LOCATION).logD("isDenied")
                permsResult.getPermanentlyDeniedPermission().logD("permanentlyDenied")

                toast("Hello")
            }
        }

//        mBinding.btnNewReq.setOnClickListener {
//            async(UI) {
//                val permsnentyDeniedPerms = permsResult.getPermanentlyDeniedPermission()
//                if (permsnentyDeniedPerms.contains(Manifest.permission.READ_CONTACTS)) {
//                    checkForPermissions(arrayListOf(Manifest.permission.READ_CONTACTS))
//                }
//            }
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                READ_REQUEST_CODE -> {  //read text file
                    if (data != null) {
                        val uri = data.data
                        uri.logD("textUri")

                        //get inputStream
                        val inputStream = contentResolver.openInputStream(uri)
                        val reader = BufferedReader(InputStreamReader(
                                inputStream))
                        val stringBuilder = StringBuilder()
                        var line: String? = ""
                        while (true) {
                            line = reader.readLine()
                            if (line == null)
                                break
                            stringBuilder.append(line)
                        }
                        mBinding.tvInputStream.text = stringBuilder


                    }
                }
                WRITE_REQUEST_CODE -> {      //write file

                }
                DELETE_REQUEST_CODE -> {    //delete text file
                    if (data != null) {
                        DocumentsContract.deleteDocument(contentResolver, data.data)
                    }
                }
                EDIT_REQUEST_CODE -> {      //edit text file
                    if (data != null) {
                        try {
                            val pfd = applicationContext.contentResolver.openFileDescriptor(data.data, "w")
                            val fileOutputStream = FileOutputStream(pfd.fileDescriptor)
                            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@StorageAccessExActivity)
                            alertDialog.setTitle("Test")
                            alertDialog.setMessage("Enter something")

                            val input = EditText(this@StorageAccessExActivity)
                            val lp = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT)
                            input.layoutParams = lp
                            alertDialog.setView(input)

                            alertDialog.setPositiveButton("Ok", { dialog: DialogInterface?, which: Int ->
                                fileOutputStream.write((input.value +
                                        System.currentTimeMillis() + "\n").toByteArray())
                                fileOutputStream.close()
                                pfd.close()

                            })

                            alertDialog.setNegativeButton("Cancel", { dialog: DialogInterface?, which: Int ->
                                dialog?.cancel()
                            })

                            alertDialog.show()


                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
                STORE_REQUEST_CODE -> {     //store text file uri in preference
                    if (data != null) {
                        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putString("URI", data.data.toString()).apply()

                        //add flag for persist permission
                        val takeFlags = data.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        applicationContext.contentResolver.takePersistableUriPermission(data.data,
                                takeFlags)
                    }
                }
                OPEN_VIDEO_REQUEST_CODE -> {    //open video file
                    if (data != null) {
                        mBinding.videoView.setVideoURI(data.data)
                        mBinding.videoView.setOnPreparedListener { mp ->
                            mp?.isLooping = true
                            mBinding.videoView.start()
                        }
                    }
                }
                OPEN_IMAGE_REQUEST_CODE -> {   //open image file
                    if (data != null) {
                        Glide.with(this@StorageAccessExActivity)
                                .load(data.data)
                                .into(mBinding.ivShow)
                    }
                }
                OPEN_AUDIO_REQUEST_CODE -> {    //open audio file
                    if (data != null) {
                        mBinding.videoView.setVideoURI(data.data)
                        mBinding.videoView.setOnPreparedListener { mp ->
                            mp?.isLooping = true
                            mBinding.videoView.start()
                        }
                    }
                }

            }
        }


    }
}