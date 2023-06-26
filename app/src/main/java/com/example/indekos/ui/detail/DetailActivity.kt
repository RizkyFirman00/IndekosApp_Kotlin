package com.example.indekos.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.indekos.databinding.ActivityDetailBinding
import com.example.indekos.ui.home.HomeActivity
import com.example.indekos.util.ViewModelFactory
import com.example.indekos.util.adapter.PhotosAdapterDetail

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private lateinit var photoAdapter: PhotosAdapterDetail
    private var photoList = mutableListOf<String>()
    private val indekosId by lazy { intent.getStringExtra("indekosId") }
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.getByIndekosId(indekosId!!.toInt()).observe(this) {
            Log.d("DetailActivity", "onCreate: $it")
            binding.tvNameDetail.text = it.name_indekos
            binding.tvPriceValue.text = it.harga
            binding.tvBedroomValue.text = it.jumlah_bedroom
            binding.tvCupboardValue.text = it.jumlah_cupboard
            binding.tvKitchenValue.text = it.jumlah_kitchen
            binding.tvAlamatValue.text = it.alamat
            binding.tvKotaValue.text = it.kota
            binding.tvProvinsiValue.text = it.provinsi
            Glide.with(this)
                .load(it.photoBannerUrl)
                .into(binding.ivPhotoBannerDetail)
            Log.d("DetailActivity", "Banner: ${it.photoUrl}")
            it.photoUrl?.let { urlPhoto -> photoList.addAll(urlPhoto) }
            photoAdapter = PhotosAdapterDetail(photoList)
            binding.rvPhotosDetail.apply {
                layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = photoAdapter
            }
        }

        binding.btnBackDetail.setOnClickListener {
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        binding.clDetail.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = binding.clDetail.scrollY
            binding.ivPhotoBannerDetail.translationY = scrollY.toFloat() / 2
        }
    }
}