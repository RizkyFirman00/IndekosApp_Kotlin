package com.example.indekos.util.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.indekos.databinding.RvKostBinding
import com.example.indekos.model.Indekos

class IndekosHomeAdapter(private val onItemClick: (String) -> Unit): ListAdapter<Indekos, IndekosHomeAdapter.IndekosViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndekosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvKostBinding.inflate(inflater, parent, false)
        return IndekosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndekosViewHolder, position: Int) {
        val indekos = getItem(position)
        holder.bind(indekos)
        holder.itemView.setOnClickListener {
            onItemClick(indekos.indekosId.toString())
        }
    }

    inner class IndekosViewHolder(private val binding: RvKostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(indekos: Indekos) {
            binding.apply {
                tvNameKost.text = indekos.name_indekos
                tvPriceValue.text = indekos.harga
                tvKota.text = indekos.kota
                tvProvinsi.text = indekos.provinsi
                ivImageKost.setImageURI(Uri.parse(indekos.photoBannerUrl))
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Indekos>() {
        override fun areItemsTheSame(oldItem: Indekos, newItem: Indekos): Boolean {
            return oldItem.indekosId == newItem.indekosId
        }

        override fun areContentsTheSame(oldItem: Indekos, newItem: Indekos): Boolean {
            return oldItem == newItem
        }
    }

}