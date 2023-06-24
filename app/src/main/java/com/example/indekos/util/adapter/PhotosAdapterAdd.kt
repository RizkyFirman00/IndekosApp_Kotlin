package com.example.indekos.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.indekos.R

class PhotosAdapterAdd(private val photoList: List<String>): RecyclerView.Adapter<PhotosAdapterAdd.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(photoFilePath: String) {
            Glide.with(itemView.context)
                .load(photoFilePath)
                .into(itemView.findViewById(R.id.img_item_photo))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapterAdd.PhotoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_photos_detail, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotosAdapterAdd.PhotoViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

}