package com.example.feistudio

import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import androidx.recyclerview.widget.RecyclerView

data class Filtro(val nombre: String)

class AdaptadorFiltros(private val datos: MutableList<Filtro>,
                       private val bitmap: Bitmap,
                       private val clickListener: (Filtro) -> Unit):
    RecyclerView.Adapter<AdaptadorFiltros.FiltroViewHolder>(){

    class FiltroViewHolder(val item: View, val bitmap: Bitmap): RecyclerView.ViewHolder(item){
        val lblFiltro = item.findViewById<TextView>(R.id.lblFiltro)
        val imgFoto = item.findViewById<ImageView>(R.id.imgFiltro)

        fun bindFiltro(filtro: Filtro){
            lblFiltro.text = filtro.nombre
            val bitmap = bitmap.scale(50,50,false)

            when(filtro.nombre){
                "Blanco y Negro" -> {
                    imgFoto.setImageBitmap(Filter.black_withe(bitmap))
                }
                "Negativo" -> {
                    imgFoto.setImageBitmap(Filter.invertirNegativo(bitmap))
                }
                "Escala Grises" -> {
                    imgFoto.setImageBitmap(Filter.grayScale(bitmap))
                }
                "Brillo" -> {
                    imgFoto.setImageBitmap(Filter.brightness(bitmap,50))
                }
                "Contraste" -> {
                    imgFoto.setImageBitmap(Filter.contrast(bitmap,50))
                }
                "Gamma" -> {
                    imgFoto.setImageBitmap(Filter.gamma(bitmap,1.8,1.8,1.8))
                }
                "Separacion de Colores" -> {
                    imgFoto.setImageBitmap(Filter.colorFilter(bitmap,2))
                }
                "Hue" -> {
                    imgFoto.setImageBitmap(Filter.applyHueFilter(bitmap,50))
                }
                "Sepian" -> {
                    imgFoto.setImageBitmap(Filter.sepian(bitmap))
                }
                "Espejo" -> {
                    imgFoto.setImageBitmap(Filter.espejo(bitmap))
                }
                "Wave" -> {
                    imgFoto.setImageBitmap(Filter.wave(bitmap))
                }
                "Sharpen" -> {
                    imgFoto.setImageBitmap(Filter.sharpen(bitmap,11.0))
                }
                "Gaussian Blur" -> {
                    imgFoto.setImageBitmap(Filter.gaussianBlur(bitmap))
                }
                "Smoothing" -> {
                    imgFoto.setImageBitmap(Filter.smooth(bitmap,1.0))
                }
                "Mean Removal" -> {
                    imgFoto.setImageBitmap(Filter.meanRemoval(bitmap))
                }
                "Embossing" -> {
                    imgFoto.setImageBitmap(Filter.embossing(bitmap))
                }
                "Edge Detection" -> {
                    imgFoto.setImageBitmap(Filter.edgeDetection(bitmap))
                }
                "Zoom" -> {
                    imgFoto.setImageBitmap(Filter.zoom(bitmap,80,100))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltroViewHolder {
        val item = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_filtro, parent, false) as LinearLayout
        return FiltroViewHolder(item, bitmap)
    }

    override fun onBindViewHolder(holder: FiltroViewHolder, position: Int) {
        val filtro = datos[position]
        holder.bindFiltro(filtro)
        holder.item.setOnClickListener { clickListener(filtro) }
    }

    override fun getItemCount(): Int = datos.size


}