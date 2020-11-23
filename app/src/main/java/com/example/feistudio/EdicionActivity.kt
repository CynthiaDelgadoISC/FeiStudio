package com.example.feistudio

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap

class EdicionActivity:Activity() {
    private lateinit var imgFoto: ImageView
    private lateinit var btnConvertir: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edicion_activity)

        btnConvertir=findViewById(R.id.btnConvertir)

        btnConvertir.setOnClickListener {
            // bitmap= AppCompatResources.getDrawable(this,R.drawable.hola) as BitmapDrawable
            //imgFoto.setImageBitmap( gaussianBlur(imagen.drawable.toBitmap()))
            Toast.makeText(applicationContext, "Imagen convertida",
                Toast.LENGTH_SHORT).show()
        }

    }

}