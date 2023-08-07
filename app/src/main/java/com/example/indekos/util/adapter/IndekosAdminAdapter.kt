package com.example.indekos.util.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.indekos.databinding.RvAdminBinding
import com.example.indekos.model.Indekos

class IndekosAdminAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<Indekos, IndekosAdminAdapter.IndekosAdminViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndekosAdminViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvAdminBinding.inflate(inflater, parent, false)
        return IndekosAdminViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndekosAdminViewHolder, position: Int) {
        val indekos = getItem(position)
        holder.bind(indekos, onItemClick)
    }

    inner class IndekosAdminViewHolder(
        private val binding: RvAdminBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(indekos: Indekos, onItemClick: (String) -> Unit) {
            binding.apply {
                tvNamaKosan.text = indekos.name_indekos
                ivFotoKosan.setImageURI(Uri.parse(indekos.photoBannerUrl))
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