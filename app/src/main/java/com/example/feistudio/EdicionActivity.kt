package com.example.feistudio

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.argb
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.graphics.*
import androidx.core.graphics.drawable.toBitmap
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
    private  lateinit var skBar:SeekBar
    private lateinit var txtValor:TextView

    private lateinit var currentPhotoPath: String
    private lateinit var btnGuardar: Button
    private lateinit var btnRevertir: Button
    private var finalBitmap: Bitmap? = null
    private var originalBitmap: Bitmap? = null

    private var separacionColoresCount: Int? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edicion_activity)
        imgFoto = findViewById(R.id.imgFoto)
        skBar=findViewById(R.id.skBar)
        txtValor=findViewById(R.id.lblPorcentaje)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnRevertir = findViewById(R.id.btnRevertir)


        skBar.isEnabled=false
        var opcion:String="brightness"
        var data: Uri? = null

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

        val filtros = listOf<Filtro>(
                Filtro("Negativo"),
                Filtro("Escala Grises"),
                Filtro("Brillo"),
                Filtro("Contraste"),
                Filtro("Gamma"),
                Filtro("Separacin de Colores"),
                Filtro("Gaussian Blur"),
                Filtro("Embossing"),
                Filtro("Sharpen"),
                Filtro("Smoothing"),
                Filtro("Blanco y Negro"),
                Filtro("Sepian"),
                Filtro("Espejo"),
                Filtro("Wave"),
                Filtro("Hue")
        )

        val adaptador = AdaptadorFiltros(filtros as MutableList<Filtro>, originalBitmap!!){
            Toast.makeText(applicationContext, "Se selecciono ${it.nombre}",
                    Toast.LENGTH_SHORT).show()
            // convertir foto
            when(it.nombre){
                "Blanco y Negro" -> {
                    finalBitmap = black_withe((imgFoto.drawable.toBitmap()))
                    imgFoto.setImageBitmap(finalBitmap)
                    skBar.isEnabled = false
                    skBar.progress = 100
                }
                "Negativo" -> {
                    finalBitmap = invertirNegativo(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                    skBar.isEnabled = false
                    skBar.progress = 100
                }
                "Escala Grises" -> {
                    finalBitmap = grayScale(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                    skBar.isEnabled = false
                    skBar.progress = 100
                }
                "Brillo" -> {
                    opcion = "brightness"
                    skBar.isEnabled = true
                    skBar.max=200
                    skBar.min=0
                    skBar.progress=100
                }
                "Contraste" -> {
                    opcion = "contrast"
                    skBar.isEnabled = true
                    skBar.progress = 0
                    skBar.max=100
                    skBar.min=0
                }
                "Gamma" -> {
                    finalBitmap = gamma(imgFoto.drawable.toBitmap(), 1.8,1.8,1.8)
                    imgFoto.setImageBitmap(finalBitmap)
                    skBar.isEnabled = false
                    skBar.progress = 100
                }
                "Separacin de Colores" -> {
                    if(separacionColoresCount == null){
                        auxBitmap = imgFoto.drawable.toBitmap()
                        separacionColoresCount = 0
                    }
                    when(separacionColoresCount){
                        0 -> {
                            finalBitmap = colorFilter(auxBitmap, 100.0,1.0,1.0)
                            separacionColoresCount = separacionColoresCount!! + 1
                        }
                        1 -> {
                            finalBitmap = colorFilter(auxBitmap, 1.0,100.0,1.0)
                            separacionColoresCount = separacionColoresCount!! + 1
                        }
                        2 -> {
                            finalBitmap = colorFilter(auxBitmap, 1.0,1.0,100.0)
                            separacionColoresCount = 0
                        }
                    }
                    imgFoto.setImageBitmap(finalBitmap)
                    skBar.isEnabled = false
                    skBar.progress = 100

                }
                "Hue" -> {
                    opcion = "applyHueFilter"
                    skBar.isEnabled = true
                    skBar.progress = 0
                    skBar.max=100
                    skBar.min=0
                }
                "Sepian" -> {
                    finalBitmap = sepian(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                    skBar.isEnabled = false
                    skBar.progress = 100
                }
                "Espejo" -> {
                    finalBitmap = espejo(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                    skBar.isEnabled = false
                    skBar.progress = 100
                }
                "Wave" -> {
                    finalBitmap = wave(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                    skBar.isEnabled = false
                    skBar.progress = 100
                }
                "Sharpen" -> {
                    finalBitmap = sharpen(imgFoto.drawable.toBitmap(), 11.0)
                    imgFoto.setImageBitmap(finalBitmap)
                    skBar.isEnabled = false
                    skBar.progress = 100
                }
                "Gaussian Blur" -> {
                    finalBitmap = gaussianBlur(imgFoto.drawable.toBitmap())
                    imgFoto.setImageBitmap(finalBitmap)
                    skBar.isEnabled = false
                    skBar.progress = 100
                }
                "Smoothing" -> {
                    finalBitmap = smooth(imgFoto.drawable.toBitmap(), 1.0)
                    imgFoto.setImageBitmap(finalBitmap)
                    skBar.isEnabled = false
                    skBar.progress = 100
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
                        if(opcion=="brightness")
                        txtValor.text = (progress - 100).toString()
                        else
                            txtValor.text=progress.toString()

                    }

                    //hace un llamado  cuando se toca la perilla
                    override fun onStartTrackingTouch(seekBar: SeekBar) {}

                    //hace un llamado  cuando se detiene la perilla
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        val valor = "" + txtValor.text
                        when (opcion) {
                            "brightness" -> {
                                finalBitmap = brightness(imgFoto.drawable.toBitmap(), valor.toInt())
                                imgFoto.setImageBitmap(finalBitmap)
                                skBar.progress = 100
                            }
                            "applyHueFilter" -> {
                                finalBitmap = applyHueFilter(imgFoto.drawable.toBitmap(), valor.toInt())
                                imgFoto.setImageBitmap(finalBitmap)
                                skBar.progress = 0
                            }
                            "contrast" -> {
                                finalBitmap = contrast(imgFoto.drawable.toBitmap(), (valor).toInt())
                                imgFoto.setImageBitmap(finalBitmap)
                                skBar.progress = 0
                            }
                        }

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

    fun invertirNegativo(src: Bitmap): Bitmap{
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        // color info
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixelColor: Int
        //Tamaño de la imagen
        val height: Int = src.height
        val width: Int = src.width
        //Obtiene y convierte cada pixel de la imagen
        for (y in 0 until height) {
            for (x in 0 until width) {
                //Obtiene un pixel
                pixelColor = src.getPixel(x, y)
                // Guarda el alpha
                A = Color.alpha(pixelColor)
                // Invierte el byte a R/G/B
                R = 255 - Color.red(pixelColor)
                G = 255 - Color.green(pixelColor)
                B = 255 - Color.blue(pixelColor)
                // Ingresa el pixel invertido en el bitmap final
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        // Retorna el bitmap final
        return bmOut
    }
    fun grayScale(src: Bitmap): Bitmap{
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        // color info
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var c:Int
        var pixel: Int
        //Tamaño de la imagen
        val height: Int = src.height
        val width: Int = src.width
        //Obtiene y convierte cada pixel de la imagen
        for (y in 0 until height) {
            for (x in 0 until width) {
                //Obtiene un pixel
                pixel = src.getPixel(x, y)
                // Guarda el alpha
                A = Color.alpha(pixel)
                // Invierte el byte a R/G/B
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)
                c=(0.299 * R + 0.587 * G + 0.114 * B).toInt()
                // Ingresa el pixel invertido en el bitmap final
                bmOut.setPixel(x, y, Color.argb(A, c, c, c))
            }
        }
        // Retorna el bitmap final
        return bmOut
    }
    fun black_withe(src: Bitmap): Bitmap {
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        var pixel: Int
        //Tamaño de la imagen
        val height: Int = src.height
        val width: Int = src.width
        //Obtiene y convierte cada pixel de la imagen
        for (y in 0 until height) {
            for (x in 0 until width) {
                //Obtiene un pixel
                pixel = src.getPixel(x, y)
                if((pixel.red + pixel.green + pixel.blue / 3) <=127)
                    bmOut.setPixel(x, y, Color.argb(pixel.alpha, 0, 0, 0))
                else
                    bmOut.setPixel(x, y, Color.argb(pixel.alpha, 255, 255, 255))

            }
        }
        // Retorna el bitmap final
        return bmOut
    }
    fun brightness(src: Bitmap, value: Int):Bitmap{
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        // color info
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int
        //Tamaño de la imagen
        val height: Int = src.height
        val width: Int = src.width
        //Obtiene y convierte cada pixel de la imagen
        for (y in 0 until height) {
            for (x in 0 until width) {
                //Obtiene un pixel
                pixel = src.getPixel(x, y)
                // Guarda el alpha
                A = Color.alpha(pixel)
                // Invierte el byte a R/G/B
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)
                //Incrementa el brillo en cada color
                R += value;
                if(R > 255) { R = 255; }
                else if(R < 0) { R = 0; }
                G += value;
                if(G > 255) { G = 255; }
                else if(G < 0) { G = 0; }
                B += value;
                if(B > 255) { B = 255; }
                else if(B < 0) { B = 0; }
                // Ingresa el pixel invertido en el bitmap final
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        // Retorna el bitmap final
        return bmOut
    }
    fun contrast(source: Bitmap, value: Int): Bitmap{
        // size
        if(value < 0){
            return source
        }
        val width = source.width
        val height = source.height
        val bmpOut = Bitmap.createBitmap(width, height, source.config)
        var A: Int; var R: Int; var G: Int; var B: Int;
        var pixel: Int
        val contrast = Math.pow(((100 + value) / 100f).toDouble(), 2.toDouble())
        println("valor: ${value}")
        println("contraste: ${contrast}")
        for(y in 0 until height){
            for(x in 0 until width){
                // obtenemos pixel
                pixel = source.getPixel(x, y)
                A = Color.alpha(pixel)
                // aplicamos el filtro solo a los canaes RGB
                R = Color.red(pixel)
                R = (((((R / 255f) - 0.5) * contrast) + 0.5) * 255f).toInt()
                if(R < 0)
                    R = 0
                else if(R > 255)
                    R = 255
                G = Color.green(pixel)
                G = (((((G / 255f) - 0.5) * contrast) + 0.5) * 255f).toInt()
                if(G < 0)
                    G = 0
                else if(G > 255)
                    G = 255
                B = Color.blue(pixel)
                B = (((((B / 255f) - 0.5) * contrast) + 0.5) * 255f).toInt()
                if(B < 0)
                    B = 0
                else if(B > 255)
                    B = 255
                // asignamos el pixel modificado al nuevo bitmap
                bmpOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        return bmpOut
    }
    fun gamma(src: Bitmap, red: Double, green: Double, blue: Double): Bitmap{
        val width = src.width
        val height = src.height

        val bmpOut = Bitmap.createBitmap(width, height, src.config)
        var A: Int; var R: Int; var G: Int; var B: Int;
        var pixel: Int

        val MAX_SIZE = 256
        val MAX_VALUE_DBL = 255.0
        val MAX_VALUE_INT = 255
        val REVERSE = 1.0

        val gammaR = arrayOfNulls<Int>(MAX_SIZE)
        val gammaG = arrayOfNulls<Int>(MAX_SIZE)
        val gammaB = arrayOfNulls<Int>(MAX_SIZE)

        for (i in 0 until MAX_SIZE){
            gammaR[i] = Math.min(MAX_VALUE_INT, (MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red) + 0.5).toInt())
            gammaG[i] = Math.min(MAX_VALUE_INT, (MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green) + 0.5).toInt())
            gammaB[i] = Math.min(MAX_VALUE_INT, (MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue) + 0.5).toInt())
        }

        for(y in 0 until height){
            for(x in 0 until width){
                pixel = src.get(x, y)
                A = Color.alpha(pixel)
                R = gammaR[Color.red(pixel)]!!
                G = gammaR[Color.green(pixel)]!!
                B = gammaR[Color.blue(pixel)]!!
                bmpOut.setPixel(x,y,Color.argb(A, R, G, B))
            }
        }

        return bmpOut

    }
    fun colorFilter(src: Bitmap, red: Double, green: Double, blue: Double): Bitmap{
        val width = src.width
        val height = src.height

        val bmpOut = Bitmap.createBitmap(width, height, src.config)
        var A: Int; var R: Int; var G: Int; var B: Int;
        var pixel: Int

        for(y in 0 until height){
            for(x in 0 until width){
                pixel = src.get(x, y)
                A = Color.alpha(pixel)
                R = (Color.red(pixel) * red).toInt()
                G = (Color.green(pixel) * green).toInt()
                B = (Color.blue(pixel) * blue).toInt()

                bmpOut.setPixel(x,y,Color.argb(A, R, G, B))
            }
        }

        return bmpOut
    }
    fun applyHueFilter(source: Bitmap, level: Int): Bitmap? {
        // get image size
        val width = source.width
        val height = source.height
        val pixels = IntArray(width * height)
        val HSV = FloatArray(3)
        // get pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height)
        var index = 0
        // iteration through pixels
        for (y in 0 until height) {
            for (x in 0 until width) {
                // get current index in 2D-matrix
                index = y * width + x
                // convert to HSV
                Color.colorToHSV(pixels[index], HSV)
                // increase Saturation level
                HSV[0] *= (level).toFloat()
                HSV[0] = Math.max((0).toFloat(), Math.min(HSV[0], (360.0).toFloat()))
                // take color back
                pixels[index] = pixels[index] or Color.HSVToColor(HSV)
            }
        }
        // output bitmap
        val bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height)
        return bmOut
    }
    fun sepian(src: Bitmap):Bitmap{
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        var nR: Int
        var nG: Int
        var nB: Int
        var pixel: Int
        //Tamaño de la imagen
        val height: Int = src.height
        val width: Int = src.width
        //Obtiene y convierte cada pixel de la imagen
        for (y in 0 until height) {
            for (x in 0 until width) {
                //Obtiene un pixel
                pixel = src.getPixel(x, y)
                nR= (0.393*pixel.red + 0.769*pixel.green + 0.189*pixel.blue).toInt()
                nG= (0.349*pixel.red + 0.686*pixel.green + 0.168*pixel.blue).toInt()
                nB= (0.272*pixel.red + 0.534*pixel.green + 0.131*pixel.blue).toInt()
                //checa la condicion
                if (nR > 255)
                    nR = 255
                if (nG > 255)
                    nG = 255
                if (nB > 255)
                    nB = 255
                // Ingresa el pixel invertido en el bitmap final
                bmOut.setPixel(x, y, Color.argb(pixel.alpha, nR, nG, nB))
            }
        }
        // Retorna el bitmap final
        return bmOut
    }
    fun espejo(src: Bitmap): Bitmap{
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        var pixelColor: Int
        //Tamaño de la imagen
        val height: Int = src.height
        val width: Int = src.width
        //Obtiene y convierte cada pixel de la imagen
        for (y in 0 until height) {
            for (x in 0 until width) {
                //Obtiene un pixel
                pixelColor = src.getPixel(width - x - 1, y)
                // Ingresa el pixel invertido en el bitmap final
                bmOut.setPixel(x, y, Color.argb(pixelColor.alpha, pixelColor.red, pixelColor.green, pixelColor.blue))
            }
        }
        // Retorna el bitmap final
        return bmOut
    }
    fun wave(src: Bitmap):Bitmap {
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        var pixel: Int
        var el: Int = 0
        var k: Int
        val height: Int = src.height
        val width: Int = src.width
        for (y in 0 until height) {
            el = y
            for (x in 0 until width) {
                pixel = src.getPixel(x, y)
                k = x
                el = (y + 20.0 * Math.sin(2.0 * Math.PI * x / 200.0)).toInt()
                //k = (x + 20.0 * Math.sin(2.0 * Math.PI * y / 200.0)).toInt()
                if (el<height && el>0)
                bmOut.setPixel(x, el, argb(pixel.alpha, pixel.red, pixel.green, pixel.blue))
                else
                    bmOut.setPixel(x, y, argb(pixel.alpha, pixel.red, pixel.green, pixel.blue))
               /* if (k<width && k>0)
                    bmOut.setPixel(k, y , Color.argb(pixel.alpha, pixel.red, pixel.green, pixel.blue))
                else
                    bmOut.setPixel(x, y , Color.argb(pixel.alpha, pixel.red, pixel.green, pixel.blue))*/
            }
        }
        return bmOut
    }
    fun sharpen(src: Bitmap, weight: Double): Bitmap? {
        val SharpConfig = arrayOf(doubleArrayOf(0.0, -2.0, 0.0), doubleArrayOf(-2.0, weight, -2.0), doubleArrayOf(0.0, -2.0, 0.0))
        val convMatrix = ConvolutionMatrix(3)
        convMatrix.applyConfig(SharpConfig)
        convMatrix.Factor = weight - 8
        return convMatrix.computeConvolution3x3(src, convMatrix)
    }
    fun gaussianBlur(src: Bitmap): Bitmap? {
        val GaussianBlurConfig = arrayOf(doubleArrayOf(1.0, 2.0, 1.0), doubleArrayOf(2.0, 4.0, 2.0), doubleArrayOf(1.0, 2.0, 1.0))
        val convMatrix = ConvolutionMatrix(3)
        convMatrix.applyConfig(GaussianBlurConfig)
        convMatrix.Factor = 16.0
        convMatrix.Offset = 0.0
        return convMatrix.computeConvolution3x3(src, convMatrix)
    }
    fun smooth(src: Bitmap, value: Double): Bitmap? {
        val convMatrix = ConvolutionMatrix(3)
        convMatrix.setAll(1.0)
        convMatrix.Matrix[1][1] = value
        convMatrix.Factor = value + 8
        convMatrix.Offset = 1.0
        return convMatrix.computeConvolution3x3(src, convMatrix)
    }
}