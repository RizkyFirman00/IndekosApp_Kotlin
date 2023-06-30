package com.example.indekos.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.indekos.R
import com.example.indekos.databinding.RvPhotosDetailBinding

class PhotosAdapterDetail(private val photoList: List<String>): RecyclerView.Adapter<PhotosAdapterDetail.PhotoViewHolder>() {

    inner class PhotoViewHolder(private val binding: RvPhotosDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photoFilePath: String) {
            Glide.with(itemView.context)
                .load(photoFilePath)
                .into(binding.imgItemPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapterDetail.PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvPhotosDetailBinding.inflate(inflater, parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosAdapterDetail.PhotoViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

}