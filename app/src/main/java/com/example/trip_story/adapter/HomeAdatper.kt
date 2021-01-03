package com.example.trip_story.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trip_story.R
import com.example.trip_story.model.Data
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.item_home.view.*

class HomeAdatper(val context : Context, var data:List<Data> ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
            return data.size
    }

    override fun onBindViewHolder   (holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position],context)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.item_home_imageview
        private val name = itemView.item_home_name
        private val time = itemView.item_home_time


        fun bind(data : Data,context: Context) {
            if(data.image != ""){
                val resourceId = context.resources.getIdentifier(data.image,"drawable",context.packageName)
                image.setImageResource(resourceId)
            }else{
                image.setImageResource(R.mipmap.ic_launcher)
            }
            name?.text = data.name
            time?.text= data.time
        }
    }
}