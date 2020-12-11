package com.example.feistudio

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap

class ZoomActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zoom_activity)
        var data2: Intent = Intent()
        var data: Uri? = null
        val bitmap: Bitmap
        val imgFoto = findViewById<ImageView>(R.id.imgView)
        val zoomView = findViewById<ZoomView>(R.id.ctrZoomView)
        if(intent.getStringExtra("pathFoto") != null && intent.getStringExtra("pathFoto") != ""){
            data = Uri.parse(intent.getStringExtra("pathFoto"))
            //imgFoto.visibility = ImageView.VISIBLE
            imgFoto.setImageURI(data).also {
                bitmap = imgFoto.drawable.toBitmap()
                imgFoto.visibility = ImageView.GONE
            }
        } else {
            data2 = intent.extras?.get("FotoCamara") as Intent
            bitmap = data2.extras?.get("data") as Bitmap
        }
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        zoomView.setBitmap(bitmap, metrics.widthPixels, metrics.heightPixels)
    }
}