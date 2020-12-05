package com.example.feistudio

import android.R.attr
import android.R.attr.bitmap
import android.R.attr.viewportHeight
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.graphics.*
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EdicionActivity:Activity() {

    private lateinit var imgFoto: ImageView
    private lateinit var recView: RecyclerView
    private  lateinit var lytTable: TableLayout
    private  lateinit var skBar:SeekBar
    private  lateinit var skBarR:SeekBar
    private  lateinit var skBarB:SeekBar
    private  lateinit var skBarG:SeekBar

    private lateinit var txtValor:TextView
    private lateinit var txtValorR:TextView
    private lateinit var txtValorB:TextView
    private lateinit var txtValorG:TextView

    private lateinit var currentPhotoPath: String
    private lateinit var btnGuardar: ImageButton
    private lateinit var btnRevertir: ImageButton
    private lateinit var btnRegresar: ImageButton
    private var finalBitmap: Bitmap? = null
    private var originalBitmap: Bitmap? = null

    private var separacionColoresCount: Int? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edicion_activity)
        imgFoto = findViewById(R.id.imgFoto)
        lytTable=findViewById(R.id.lyTable)
        skBar=findViewById(R.id.skBar)
        skBarR=findViewById(R.id.skBarR)
        skBarB=findViewById(R.id.skBarB)
        skBarG=findViewById(R.id.skBarG)
        txtValor=findViewById(R.id.lblPorcentaje)
        txtValorR=findViewById(R.id.lblR)
        txtValorB=findViewById(R.id.lblB)
        txtValorG=findViewById(R.id.lblG)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnRevertir = findViewById(R.id.btnRevertir)
        btnRegresar = findViewById(R.id.btnRegresar)


        skBar.isEnabled=false
        var opcion:String="no"
        var data: Uri?

        if(intent.getStringExtra("pathFoto") != null && intent.getStringExtra("pathFoto") != ""){
            data = Uri.parse(intent.getStringExtra("pathFoto"))
            imgFoto.setImageURI(data).also {
                originalBitmap = imgFoto.drawable.toBitmap()
            }
        } else {
            val data2 = intent.extras?.get("FotoCamara") as Intent
            originalBitmap = data2.extras?.get("data") as Bitmap
            imgFoto.setImageBitmap(originalBitmap)
        }
        var auxBitmap: Bitmap = originalBitmap!!

        recView = findViewById(R.id.recViewFiltros)

        findViewById<LinearLayout>(R.id.fondo).background =
                GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(
                                Color.parseColor("#FFFFFF"),
                                Color.parseColor("#E3E3E3"),
                                colorFondo(originalBitmap!!)
                        )
                )


        val filtros = listOf<Filtro>(
                Filtro("Negativo"),
                Filtro("Escala Grises"),
                Filtro("Brillo"),
                Filtro("Contraste"),
                Filtro("Gamma"),
                Filtro("Separacion de Colores"),
                Filtro("Smoothing"),
                Filtro("Gaussian Blur"),
                Filtro("Sharpen"),
                Filtro("Mean Removal"),
                Filtro("Embossing"),
                Filtro("Edge Detection"),
                Filtro("Zoom"),
                Filtro("Blanco y Negro"),
                Filtro("Sepian"),
                Filtro("Espejo"),
                Filtro("Wave"),
                Filtro("Hue")
        )

        val adaptador = AdaptadorFiltros(filtros as MutableList<Filtro>, originalBitmap!!){
            Toast.makeText(applicationContext, "Se selecciono ${it.nombre}", Toast.LENGTH_SHORT).show()
            // convertir foto
            lytTable.visibility= View.GONE
            skBar.isEnabled = false
            skBar.visibility= View.GONE
            skBar.progress = 0
            txtValor.visibility=View.GONE
            when(it.nombre){
                "Blanco y Negro" -> {
                    finalBitmap = Filter.black_withe((imgFoto.drawable.toBitmap()))
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Negativo" -> {
                    finalBitmap = Filter.invertirNegativo(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Escala Grises" -> {
                    finalBitmap = Filter.grayScale(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Brillo" -> {
                    opcion = "brightness"
                    skBar.isEnabled = true
                    skBar.visibility= View.VISIBLE
                    skBar.max = 200
                    skBar.min = 0
                    skBar.progress = 100
                }
                "Contraste" -> {
                    opcion = "contrast"
                    skBar.isEnabled = true
                    skBar.visibility= View.VISIBLE
                    skBar.max = 200
                    skBar.min = 0
                    skBar.progress = 100
                }
                "Gamma" -> {
                    finalBitmap = Filter.gamma(imgFoto.drawable.toBitmap(), 1.8, 1.8, 1.8)
                    imgFoto.setImageBitmap(finalBitmap)
                    lytTable.visibility= View.VISIBLE
                }
                "Separacion de Colores" -> {
                    if (separacionColoresCount == null) {
                        auxBitmap = imgFoto.drawable.toBitmap()
                        separacionColoresCount = 0
                    }
                    when (separacionColoresCount) {
                        0 -> {
                            finalBitmap = Filter.colorFilter(auxBitmap, 0)
                            separacionColoresCount = separacionColoresCount!! + 1
                        }
                        1 -> {
                            finalBitmap = Filter.colorFilter(auxBitmap, 1)
                            separacionColoresCount = separacionColoresCount!! + 1
                        }
                        2 -> {
                            finalBitmap = Filter.colorFilter(auxBitmap, 2)
                            separacionColoresCount = 0
                        }
                    }
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Hue" -> {
                    opcion = "hue"
                    skBar.isEnabled = true
                    skBar.visibility= View.VISIBLE
                    skBar.progress = 0
                    skBar.max = 100
                    skBar.min = 0
                }
                "Sepian" -> {
                    finalBitmap = Filter.sepian(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Espejo" -> {
                    finalBitmap = Filter.espejo(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Wave" -> {
                    finalBitmap = Filter.wave(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Sharpen" -> {
                    finalBitmap = Filter.sharpen(imgFoto.drawable.toBitmap(), 11.0)
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Gaussian Blur" -> {
                    finalBitmap = Filter.gaussianBlur(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Smoothing" -> {
                    finalBitmap = Filter.smooth(imgFoto.drawable.toBitmap(), 1.0)
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Mean Removal" -> {
                    finalBitmap = Filter.meanRemoval(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Embossing" -> {
                    finalBitmap = Filter.embossing(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Edge Detection" -> {
                    finalBitmap = Filter.edgeDetection(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                }
                "Zoom" -> {
                    finalBitmap = Filter.zoom(imgFoto.drawable.toBitmap(),600,1000)
                    imgFoto.setImageBitmap(finalBitmap)
                }

            }


        }

        recView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recView.adapter = adaptador


        skBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    //hace un llamado a la perilla cuando se arrastra
                    override fun onProgressChanged(seekBar: SeekBar,
                                                   progress: Int, fromUser: Boolean) {
                        if(skBar.isVisible) {
                            txtValor.visibility= View.VISIBLE
                            txtValor.text="0"
                            if (opcion == "hue")
                                txtValor.text = progress.toString()
                            else
                                txtValor.text = (progress - 100).toString()
                        }
                        else {
                            txtValor.visibility= View.GONE
                        }
                    }
                    //hace un llamado  cuando se toca la perilla
                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    //hace un llamado  cuando se detiene la perilla
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        val valor = "" + txtValor.text
                        when (opcion) {
                            "brightness" -> {
                                finalBitmap = Filter.brightness(imgFoto.drawable.toBitmap(), valor.toInt())
                                imgFoto.setImageBitmap(finalBitmap)
                                skBar.progress = 100
                            }
                            "hue" -> {
                                finalBitmap = Filter.applyHueFilter(imgFoto.drawable.toBitmap(), valor.toInt())
                                imgFoto.setImageBitmap(finalBitmap)
                                skBar.progress = 0
                            }
                            "contrast" -> {
                                finalBitmap = Filter.contrast(imgFoto.drawable.toBitmap(), (valor).toInt())
                                imgFoto.setImageBitmap(finalBitmap)
                                skBar.progress = 100
                            }
                        }
                    }
                })
        skBarR.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    //hace un llamado a la perilla cuando se arrastra
                    override fun onProgressChanged(seekBar: SeekBar,
                                                   progress: Int, fromUser: Boolean) {
                        txtValorR.text = (progress.toFloat()/40.0).toString()
                    }
                    //hace un llamado  cuando se toca la perilla
                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    //hace un llamado  cuando se detiene la perilla
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        val valorR = "" + txtValorR.text
                        val valorG = "" + txtValorG.text
                        val valorB = "" + txtValorB.text
                        finalBitmap = Filter.gamma(imgFoto.drawable.toBitmap(), valorR.toDouble(), valorG.toDouble(), valorB.toDouble())
                        imgFoto.setImageBitmap(finalBitmap)
                    }
                })
        skBarG.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    //hace un llamado a la perilla cuando se arrastra
                    override fun onProgressChanged(seekBar: SeekBar,
                                                   progress: Int, fromUser: Boolean) {
                        txtValorG.text = (progress.toFloat()/40.0).toString()
                    }
                    //hace un llamado  cuando se toca la perilla
                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    //hace un llamado  cuando se detiene la perilla
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        val valorR = "" + txtValorR.text
                        val valorG = "" + txtValorG.text
                        val valorB = "" + txtValorB.text
                        finalBitmap = Filter.gamma(imgFoto.drawable.toBitmap(), valorR.toDouble(), valorG.toDouble(), valorB.toDouble())
                        imgFoto.setImageBitmap(finalBitmap)
                    }
                })
        skBarB.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    //hace un llamado a la perilla cuando se arrastra
                    override fun onProgressChanged(seekBar: SeekBar,
                                                   progress: Int, fromUser: Boolean) {
                        txtValorB.text = (progress.toFloat()/40.0).toString()
                    }
                    //hace un llamado  cuando se toca la perilla
                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    //hace un llamado  cuando se detiene la perilla
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        val valorR = "" + txtValorR.text
                        val valorG = "" + txtValorG.text
                        val valorB = "" + txtValorB.text
                        finalBitmap = Filter.gamma(imgFoto.drawable.toBitmap(), valorR.toDouble(), valorG.toDouble(), valorB.toDouble())
                        imgFoto.setImageBitmap(finalBitmap)
                    }
                })

        btnGuardar.setOnClickListener {
            val bytes = ByteArrayOutputStream()
            finalBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException){
                Toast.makeText(this, "Problema al guardar la foto: ${ex}", Toast.LENGTH_SHORT).show()
                null
            }
            if(photoFile != null){
                MediaStore.Images.Media.insertImage(this.contentResolver, finalBitmap, currentPhotoPath, "FeiStudio")
                Toast.makeText(this, "Imagen guardada!", Toast.LENGTH_SHORT).show()
            }
        }

        btnRevertir.setOnClickListener{
            imgFoto.setImageBitmap(originalBitmap)
        }
        btnRegresar.setOnClickListener{
            val i = Intent(this, MainActivity::class.java)
            finish()
            startActivity(i)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* tipo */
                storageDir /* directorio en el que se guardara, en este caso en el directorio de Pictures */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun colorFondo(src: Bitmap): Int {
        val width = src.width
        val height = src.height
        var A = 0
        var R = 0
        var G = 0
        var B = 0
        val pixelCount = width * height
        val hasAlpha = src.hasAlpha()

        val pixels = IntArray(pixelCount)

        src.getPixels(pixels, 0, width, 0, 0, width, height)

        for ( y in 0 until height){
            for (x in 0 until width){
                val color: Int = pixels.get(x + y) // x + y * width
                //val color: Int = src.getPixel(x,y) // x + y * width

                R += color shr 16 and 0xFF // Color.red

                G += color shr 8 and 0xFF // Color.greed

                B += color and 0xFF // Color.blue
                if (hasAlpha) A += color ushr 24 // Color.alpha

            }
        }
        return Color.argb(
                if (hasAlpha) A / pixelCount else 255,
                R / pixelCount,
                G / pixelCount,
                B / pixelCount
        )
    }


}