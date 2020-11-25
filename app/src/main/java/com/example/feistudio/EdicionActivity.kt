package com.example.feistudio

import android.app.Activity
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EdicionActivity:Activity() {

    private lateinit var imgFoto: ImageView
    private lateinit var btnConvertir: Button
    private lateinit var recView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edicion_activity)
        imgFoto = findViewById(R.id.imgFoto)

        val data = Uri.parse(intent.getStringExtra("pathFoto"))

        imgFoto.setImageURI(data)


        recView = findViewById(R.id.recViewFiltros)

        val filtros = listOf<Filtro>(Filtro("Blanco y Negro"),
                Filtro("Negativo"),
                Filtro("Brillo"),
                Filtro("Gaussian Blur"),
                Filtro("Embossing"),
                Filtro("Smoothing")
        )

        val adaptador = AdaptadorFiltros(filtros as MutableList<Filtro>, data){
            Toast.makeText(applicationContext, "Se selecciono ${it.nombre}",
                    Toast.LENGTH_SHORT).show()
            // convertir foto
            // ....

        }

        recView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recView.adapter = adaptador

        btnConvertir=findViewById(R.id.btnConvertir)

        btnConvertir.setOnClickListener {
            // bitmap= AppCompatResources.getDrawable(this,R.drawable.hola) as BitmapDrawable
            //imgFoto.setImageBitmap( gaussianBlur(imagen.drawable.toBitmap()))
            Toast.makeText(applicationContext, "Imagen convertida",
                Toast.LENGTH_SHORT).show()
        }

    }

}