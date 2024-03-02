package com.example.seeon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.seeon.R
import com.google.android.material.imageview.ShapeableImageView

class GenresAdapter(val context : Context, val genresList: ArrayList<Int>):
    RecyclerView.Adapter<GenresAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(position){
            0-> holder.genreIcon.setBackgroundColor(ContextCompat.getColor(context, R.color.lightLightBlue))
            1-> holder.genreIcon.setBackgroundColor(ContextCompat.getColor(context, R.color.lightBlue))
            2-> holder.genreIcon.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
            3-> holder.genreIcon.setBackgroundColor(ContextCompat.getColor(context, R.color.lightPurple))
            4-> holder.genreIcon.setBackgroundColor(ContextCompat.getColor(context, R.color.pink))
            5-> holder.genreIcon.setBackgroundColor(ContextCompat.getColor(context, R.color.lightLightLightBlue))
        }
        holder.genreIcon.setImageResource(genresList[position])
    }

    override fun getItemCount()=genresList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val genreIcon:ShapeableImageView = itemView.findViewById(R.id.genreIcon)
    }
}