package com.example.feistudio

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.Serializable



class MainActivity : AppCompatActivity() {
    private var REQUEST_IMAGE_GALLERY=101
    private var REQUEST_PERMISSION_CODE=100
    private var REQUEST_PERMISSION_CAMERA=102
    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnGaleria: Button=findViewById(R.id.btnGaleria)
        val btnCamara: Button=findViewById(R.id.btnCamara)

        btnGaleria.setOnClickListener {
            //Verifa que version de android se tiene
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                //Pregunta si tiene permiso
                if(
                        ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED ||
                        ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED
                ){
                    //Pide permiso
                    val permisoGaleria= arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permisoGaleria,REQUEST_PERMISSION_CODE)
                }
                else{
                    openGallery()
                }
            }else{
                openGallery()
            }
        }
        btnCamara.setOnClickListener {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                //Pregunta si tiene permiso
                if(
                        ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ){
                    //Pide permiso
                    val permisoGaleria= arrayOf(android.Manifest.permission.CAMERA)
                    requestPermissions(permisoGaleria,REQUEST_PERMISSION_CAMERA)
                }
                else{
                    openCamera()
                }
            }else{
                openCamera()
            }
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_PERMISSION_CODE->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    openGallery()
                else
                    Toast.makeText(applicationContext, "No se puede acceder a la galeria",
                            Toast.LENGTH_SHORT).show()
            }
            REQUEST_PERMISSION_CAMERA->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    openCamera()
                else
                    Toast.makeText(applicationContext, "No se puede acceder a la camara",
                            Toast.LENGTH_SHORT).show()
            }
        }
    }
    //Abre la venta de galeria
    private fun openGallery(){
        val intent= Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent,REQUEST_IMAGE_GALLERY)
    }

    private fun openCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK ){
            when(requestCode){
                REQUEST_IMAGE_GALLERY -> {
                    val i=Intent(this, EdicionActivity::class.java)
                    //Tomar el nombre de la foto seleccionada
                    val filePath = data?.data.toString()
                    i.putExtra("pathFoto",filePath)
                    startActivity(i)
                }
                REQUEST_IMAGE_CAPTURE -> {
                    //val imageBitmap = data.extras?.get("data")
                    val i = Intent(this, EdicionActivity::class.java)
                    i.putExtra("FotoCamara", data)
                    startActivity(i)
                }
            }

        }
    }
}