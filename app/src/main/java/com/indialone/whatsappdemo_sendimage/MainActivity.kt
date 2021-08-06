package com.indialone.whatsappdemo_sendimage

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneNumberUtils
import android.widget.Toast
import com.indialone.whatsappdemo_sendimage.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    val PICK_IMAGE_CODE = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.button.setOnClickListener {
            askPermission()
        }

    }

    private fun askPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, PICK_IMAGE_CODE)

                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(this@MainActivity, "You denied permission ", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest?,
                    permissionToken: PermissionToken?
                ) {
                    permissionToken!!.continuePermissionRequest()
                }

            }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_CODE) {
                if (data != null) {
                    val image = data.data
                    val number = "919429957789"
                    val message = "I am sending the image for testing"
                    val installed = whatsappInstalledOrNot("com.whatsapp")
                    if (installed) {
                        val whatsappIntent = Intent(Intent.ACTION_SEND)
                        whatsappIntent.setPackage("com.whatsapp")
                        whatsappIntent.setType("text/plain")
                        whatsappIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            message
                        )
                        whatsappIntent.putExtra(Intent.EXTRA_STREAM, image)
                        whatsappIntent.setType("image/*")
                        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        whatsappIntent.putExtra(
                            "jid",
                            PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net"
                        ) //phone number without "+" prefix
                        Log.e("whatsapi","${PhoneNumberUtils.stripSeparators(number)}@s.whatsapp.net")
                        startActivity(whatsappIntent)
                    }

                }
            }
        }

    }

    private fun whatsappInstalledOrNot(uri: String): Boolean {
        val pm = packageManager
        var app_installed = false
        app_installed = try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }


}

/*
    working code for WhatsAap for saving numbers

    val whatsappIntent = Intent(Intent.ACTION_SEND)
                        whatsappIntent.setType("text/plain")
                        whatsappIntent.setPackage("com.whatsapp")
                        whatsappIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "I am sending the image for testing"
                        )
                        whatsappIntent.putExtra(Intent.EXTRA_STREAM, image)
                        whatsappIntent.setType("image/*")
                        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        try {
                            startActivity(whatsappIntent)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }


 */

 /*
 //                    else {
//                        Toast.makeText(this, "Whatsapp not installed", Toast.LENGTH_SHORT)
//                            .show()
//                        val uri = Uri.parse("market://details?id=com.whatsapp")
//                        val gotoMarket = Intent(Intent.ACTION_VIEW, uri)
//                        startActivity(gotoMarket)
//                    }
 */
 */