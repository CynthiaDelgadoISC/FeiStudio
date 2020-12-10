package com.example.feistudio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.graphics.scale
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ZoomView: View {

    private lateinit var extraCanvas: Canvas
    private lateinit var extraAuxBitmap: Bitmap
    private lateinit var extraBitmap: Bitmap

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var motionTouchEventX2 = 0f
    private var motionTouchEventY2 = 0f

    private var indexFirstPointer: Int = -1
    private var indexSecondPointer: Int = -1

    private var distance = 0.0
    private var currenDistance = 0.0
    private var relacion = 0.0
    private var absoluteWidth = 0
    private var absoluteHeight = 0
    var offsetX = 0f
    var offsetY = 0f
    var offsetHeight = 0f
    private var currentY = 0f



    constructor(context: Context): super(context){
    }
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttrs: Int): super(context, attributeSet, defStyleAttrs){
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        extraBitmap = extraAuxBitmap
        extraCanvas = Canvas()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        try {
            canvas?.drawBitmap(extraBitmap, offsetX, offsetY, null)
        } catch (e: Exception){
            println(e.message)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        println("Action: ${event!!.actionMasked}")
        when(event!!.actionMasked){
            MotionEvent.ACTION_DOWN ->{
                indexFirstPointer = event.getPointerId(event.actionIndex)
                motionTouchEventX = event.getX(indexFirstPointer)
                motionTouchEventY = event.getY(indexFirstPointer)
                println("actionIndexFirst: "+ event.actionIndex)
                println("X: " + event.getX(indexFirstPointer))
                println("Y: " +event.getY(indexFirstPointer))
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                indexSecondPointer = event.getPointerId(event.actionIndex)
                event.findPointerIndex(indexSecondPointer).let { pointerIndex ->
                    motionTouchEventX2 = event.getX(pointerIndex)
                    motionTouchEventY2 = event.getY(pointerIndex)
                }
                currenDistance = Math.sqrt(Math.pow((motionTouchEventX2 - motionTouchEventX).toDouble(), 2.0) + Math.pow((motionTouchEventY2 - motionTouchEventY).toDouble(), 2.0))
                println("actionIndexSecond: "+indexSecondPointer)
                println("X: " + event.getX(indexSecondPointer))
                println("Y: " +event.getY(indexSecondPointer))
            }
            MotionEvent.ACTION_MOVE -> {
                if(event.actionIndex == indexFirstPointer){
                    motionTouchEventX = event.getX(event.actionIndex)
                    motionTouchEventY = event.getY(event.actionIndex)
                    println("X: " + event.getX(indexFirstPointer))
                    println("Y: " +event.getY(indexFirstPointer))
                } else if(event.actionIndex == indexSecondPointer){
                    motionTouchEventX2 = event.getX(event.actionIndex)
                    motionTouchEventY2 = event.getY(event.actionIndex)
                    println("X2: " + event.getX(indexSecondPointer))
                    println("Y2: " +event.getY(indexSecondPointer))
                }
                println("currentDistance: ${currenDistance}")
                println("distance: $distance")
                distance = Math.sqrt(Math.pow((motionTouchEventX2 - motionTouchEventX).toDouble(), 2.0) + Math.pow((motionTouchEventY2 - motionTouchEventY).toDouble(), 2.0))
                distance = Math.abs(distance)
                relacion = distance / currenDistance
                println("relacion: $relacion")
            }
            MotionEvent.ACTION_UP -> {
                val oldWidth = extraBitmap.width
                val oldHeight = extraBitmap.height
                val newWidth = relacion * extraBitmap.width
                val newheight = relacion * extraBitmap.height
                //extraBitmap = extraBitmap.scale(newWidth.toInt(), newheight.toInt(), false)

                println("newWidth: $newWidth")
                println("newHeight: $newheight")
                if (newWidth > absoluteWidth) {
                    offsetX = (((newWidth - absoluteWidth) / 2) * -1).toFloat()
                } else if (newWidth < absoluteWidth) {
                    offsetX = (((absoluteWidth - newWidth) / 2).toFloat())
                }
                if (newheight > absoluteHeight) {
                    offsetY = (((newheight - absoluteHeight) / 2) * -1).toFloat()
                } else if (newheight < absoluteHeight) {
                    offsetY = (((absoluteHeight - newheight) / 2).toFloat())
                }
                offsetY = offsetY + offsetHeight
                println("absoluteWidth: $absoluteWidth")
                println("absoluteHeight: $absoluteHeight")
                println("offsetX: ${offsetX.toFloat()}")
                println("offsetY: ${offsetY.toFloat()}")
                Toast.makeText(context, "Procesando imagen", Toast.LENGTH_SHORT).show()

                if (newWidth > 0 && newheight > 0)
                    extraBitmap = Filter.zoom(extraBitmap, newWidth.toInt(), newheight.toInt())
                extraCanvas.drawBitmap(extraBitmap, offsetX, offsetY, null)
                    .let {
                        Toast.makeText(context, "se modifico la imagen", Toast.LENGTH_SHORT).show()
                        println("Listoooooooo")
                    }
                invalidate()
            }
        }

        return true
    }

    fun setBitmap(bitmap: Bitmap, absWidth: Int, absHeight: Int, offset: Float){
        extraAuxBitmap = bitmap
        extraBitmap = bitmap
        absoluteWidth = absWidth
        absoluteHeight = absHeight
        offsetHeight = offset
    }

}