package com.example.feistudio

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.*
import kotlin.math.floor

class Filter {
    companion object{
        fun invertirNegativo(src: Bitmap): Bitmap {
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
            val width = source.width
            val height = source.height
            val bmpOut = Bitmap.createBitmap(width, height, source.config)
            var A: Int; var R: Int; var G: Int; var B: Int;
            var pixel: Int
            val contrast = Math.pow(((100 + value) / 100f).toDouble(), 2.toDouble())
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
        fun colorFilter(src: Bitmap, color: Int): Bitmap{
            val width = src.width
            val height = src.height

            val bmpOut = Bitmap.createBitmap(width, height, src.config)
            var pixel: Int

            for(y in 0 until height){
                for(x in 0 until width){
                    pixel = src.get(x, y)
                    when(color){
                        0 ->  bmpOut.setPixel(x,y,Color.argb(pixel.alpha, pixel.red, 0, 0))
                        1 ->  bmpOut.setPixel(x,y,Color.argb(pixel.alpha, 0, pixel.green, 0))
                        2 ->  bmpOut.setPixel(x,y,Color.argb(pixel.alpha, 0, 0, pixel.blue))
                    }
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
                        bmOut.setPixel(x, el, Color.argb(pixel.alpha, pixel.red, pixel.green, pixel.blue))
                    else
                        bmOut.setPixel(x, y, Color.argb(pixel.alpha, pixel.red, pixel.green, pixel.blue))
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
        fun meanRemoval(src: Bitmap): Bitmap? {
            val MeanRemovalConfig = arrayOf(doubleArrayOf(-1.0, -1.0, -1.0), doubleArrayOf(-1.0, 9.0, -1.0), doubleArrayOf(-1.0, -1.0, -1.0))
            val convMatrix = ConvolutionMatrix(3)
            convMatrix.applyConfig(MeanRemovalConfig)
            convMatrix.Factor = 1.0
            convMatrix.Offset = 0.0
            return convMatrix.computeConvolution3x3(src, convMatrix)
        }
        fun embossing(src: Bitmap): Bitmap? {
            val embossingConfig = arrayOf(doubleArrayOf(-1.0, 0.0, -1.0), doubleArrayOf(0.0, 4.0, 0.0), doubleArrayOf(-1.0, 0.0, -1.0))
            val convMatrix = ConvolutionMatrix(3)
            convMatrix.applyConfig(embossingConfig)
            convMatrix.Factor = 1.0
            convMatrix.Offset = 127.0
            return convMatrix.computeConvolution3x3(src, convMatrix)
        }
        fun edgeDetection(src: Bitmap): Bitmap? {
            val edgeDetectionConfig = arrayOf(doubleArrayOf(0.0,1.0,0.0), doubleArrayOf(1.0,-4.0,1.0), doubleArrayOf(0.0,1.0,0.0))
            //val edgeDetectionConfig = arrayOf(doubleArrayOf(0.0,0.0,0.0), doubleArrayOf(-1.0,1.0,0.0), doubleArrayOf(0.0,0.0,0.0))
            val convMatrix = ConvolutionMatrix(3)
            convMatrix.applyConfig(edgeDetectionConfig)
            convMatrix.Factor = 1.0
            convMatrix.Offset = 0.0
            return convMatrix.computeConvolution3x3(src, convMatrix)
        }
        fun zoom(bitmap: Bitmap, nWidth: Int, nHeight: Int): Bitmap{
            var bTemp = bitmap.copy(bitmap.config,true)
            var newBitmap = Bitmap.createBitmap(nWidth,nHeight,bitmap.config)

            val nXFactor: Double = bTemp.width.toDouble()/nWidth
            val nYFactor: Double = bTemp.height.toDouble()/nHeight

            for(x in 0 until newBitmap.width-1){
                for(y in 0 until newBitmap.height-1){
                    var floor_x: Int = floor(x * nXFactor).toInt()
                    var floor_y = floor(y * nYFactor).toInt()
                    var ceil_x = floor_x + 1

                    if (ceil_x >= bTemp.width) ceil_x = floor_x;
                    var ceil_y = floor_y + 1;
                    if (ceil_y >= bTemp.height) ceil_y = floor_y;

                    var fraction_x = x * nXFactor - floor_x;
                    var fraction_y = y * nYFactor - floor_y;
                    var one_minus_x = 1.0 - fraction_x;
                    var one_minus_y = 1.0 - fraction_y;



                    var c1 = Color.rgb(
                        Color.red(bTemp.getPixel(floor_x, floor_y)),
                        Color.green(bTemp.getPixel(floor_x, floor_y)),
                        Color.blue(bTemp.getPixel(floor_x, floor_y))
                    )
                    var c2 = Color.rgb(
                        Color.red(bTemp.getPixel(ceil_x, floor_y)),
                        Color.green(bTemp.getPixel(ceil_x, floor_y)),
                        Color.blue(bTemp.getPixel(ceil_x, floor_y))
                    )
                    var c3 = Color.rgb(
                        Color.red(bTemp.getPixel(floor_x, ceil_y)),
                        Color.green(bTemp.getPixel(floor_x, ceil_y)),
                        Color.blue(bTemp.getPixel(floor_x, ceil_y))
                    )
                    var c4 = Color.rgb(
                        Color.red(bTemp.getPixel(ceil_x, ceil_y)),
                        Color.green(bTemp.getPixel(ceil_x, ceil_y)),
                        Color.blue(bTemp.getPixel(ceil_x, ceil_y))
                    )

                    //AZUL
                    var b1 = (one_minus_x * c1.blue + fraction_x * c2.blue);
                    var b2 = (one_minus_x * c3.blue + fraction_x * c4.blue);

                    val blue = (one_minus_y * (b1) + fraction_y * (b2)).toInt();

                    //VERDE
                    b1 = (one_minus_x * c1.green + fraction_x * c2.green);
                    b2 = (one_minus_x * c3.green + fraction_x * c4.green);

                    val green = (one_minus_y * (b1) + fraction_y * (b2)).toInt();

                    //ROJO
                    b1 = (one_minus_x * c1.red + fraction_x * c2.red);
                    b2 = (one_minus_x * c3.red + fraction_x * c4.red);

                    val red = (one_minus_y * (b1) + fraction_y * (b2)).toInt();

                    newBitmap.setPixel(x,y,Color.rgb(red,green,blue))
                }
            }

            return newBitmap
        }
    }
}