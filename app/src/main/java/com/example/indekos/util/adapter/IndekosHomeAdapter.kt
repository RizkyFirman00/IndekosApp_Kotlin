package com.example.indekos.util.adapter

import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.indekos.databinding.RvKostBinding
import com.example.indekos.model.Indekos
import com.example.indekos.model.Users
import com.example.indekos.ui.home.HomeActivity
import java.lang.Math.atan2
import java.lang.Math.sin
import java.lang.Math.sqrt
import java.lang.StrictMath.cos
import kotlin.math.pow

class IndekosHomeAdapter(
    private val onItemClick: (String) -> Unit)
    : ListAdapter<Indekos, IndekosHomeAdapter.IndekosViewHolder>(DiffCallback()) {

    private val jarakMap: HashMap<Int, String> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndekosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvKostBinding.inflate(inflater, parent, false)
        return IndekosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndekosViewHolder, position: Int) {
        val indekos = getItem(position)
        holder.bind(indekos, onItemClick)
    }

    fun updateJarak(indekosId: Int, jarak: String) {
        jarakMap[indekosId] = jarak
        notifyDataSetChanged()
    }

    inner class IndekosViewHolder(
        private val binding: RvKostBinding,
        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(indekos: Indekos, onItemClick: (String) -> Unit) {
            binding.apply {
                tvNameKost.text = indekos.name_indekos
                tvPriceValue.text = indekos.harga
                val jarak = jarakMap[indekos.indekosId]
                tvJarak.text = "$jarak m"
                ivImageKost.setImageURI(Uri.parse(indekos.photoBannerUrl))
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