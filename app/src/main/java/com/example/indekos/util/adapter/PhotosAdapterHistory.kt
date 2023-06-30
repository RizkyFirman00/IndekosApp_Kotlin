package com.example.indekos.util.adapter

import android.view.ContextMenu
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.indekos.databinding.RvPhotosDetailBinding
import java.io.File

class PhotosAdapterHistory(private val photoList: MutableList<String>) :
    RecyclerView.Adapter<PhotosAdapterHistory.PhotoViewHolder>() {

    inner class PhotoViewHolder(private val binding: RvPhotosDetailBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        private lateinit var photoFilePath: String

        init {
            binding.root.setOnCreateContextMenuListener(this)
        }

        fun bind(photoFilePath: String) {
            this.photoFilePath = photoFilePath
            Glide.with(itemView.context)
                .load(photoFilePath)
                .into(binding.imgItemPhoto)

            binding.root.setOnLongClickListener {
                itemView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                Toast.makeText(
                    itemView.context,
                    "Foto berhasi dihapus",
                    Toast.LENGTH_SHORT
                ).show()
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    photoList.removeAt(position)
                    notifyItemRemoved(position)
                }
                true
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(0, adapterPosition, 0, "Hapus")
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            return when (item?.itemId) {
                0 -> {
                    deletePhoto()
                    true
                }
                else -> false
            }
        }

        private fun deletePhoto() {
            val file = File(photoFilePath)
            if (file.exists()) {
                file.delete()
                photoList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotosAdapterHistory.PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvPhotosDetailBinding.inflate(inflater, parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosAdapterHistory.PhotoViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

}