package com.example.feistudio

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore

val REQUEST_IMAGE_CAPTURE = 1

class CamaraActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dispatchTakePictureIntent()
    }

    private fun dispatchTakePictureIntent() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data.extras?.get("data")
            val i = Intent(this, EdicionActivity::class.java)
            i.putExtra("FotoCamara", data)
            startActivity(i)
            setResult(RESULT_OK)
        }
    }

}