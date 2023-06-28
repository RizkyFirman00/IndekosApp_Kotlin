package com.example.indekos.util.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.indekos.databinding.RvDataHistoryBinding
import com.example.indekos.model.Indekos

class IndekosHistoryAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<Indekos, IndekosHistoryAdapter.IndekosHistoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndekosHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvDataHistoryBinding.inflate(inflater, parent, false)
        return IndekosHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndekosHistoryViewHolder, position: Int) {
        val indekos = getItem(position)
        holder.bind(indekos, onItemClick)
    }

    inner class IndekosHistoryViewHolder(
        private val binding: RvDataHistoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(indekos: Indekos, onItemClick: (String) -> Unit) {
            binding.apply {
                tvNameValue.text = indekos.name_indekos
                tvPriceValue.text = indekos.harga
                ivImageValue.setImageURI(Uri.parse(indekos.photoBannerUrl))
                root.setOnClickListener {
                    onItemClick.invoke(indekos.indekosId.toString())
                }
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