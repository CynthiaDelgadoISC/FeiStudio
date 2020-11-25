package com.example.feistudio

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
                       private val data: Uri,
                       private val clickListener: (Filtro) -> Unit):
    RecyclerView.Adapter<AdaptadorFiltros.FiltroViewHolder>(){

    class FiltroViewHolder(val item: View, val data: Uri): RecyclerView.ViewHolder(item){
        val lblFiltro = item.findViewById<TextView>(R.id.lblFiltro)
        val imgFoto = item.findViewById<ImageView>(R.id.imgFiltro)

        fun bindFiltro(filtro: Filtro){
            lblFiltro.text = filtro.nombre

            imgFoto.setImageURI(data)
            val bitmap = imgFoto.drawable.toBitmap().scale(70,70,false)
            imgFoto.setImageBitmap(bitmap)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltroViewHolder {
        val item = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_filtro, parent, false) as LinearLayout
        return FiltroViewHolder(item, data)
    }

    override fun onBindViewHolder(holder: FiltroViewHolder, position: Int) {
        val filtro = datos[position]
        holder.bindFiltro(filtro)
        holder.item.setOnClickListener { clickListener(filtro) }
    }

    override fun getItemCount(): Int = datos.size


}