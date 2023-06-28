package com.example.indekos.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
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
    private var userId: Int? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var label: String = ""
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private var bannerHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.ivPhotoBannerDetail.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                binding.ivPhotoBannerDetail.viewTreeObserver.removeOnPreDrawListener(this)
                bannerHeight = binding.ivPhotoBannerDetail.height
                return true
            }
        })

        binding.btnMaps.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($label)")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        viewModel.getByIndekosId(indekosId!!.toInt()).observe(this) {

            Log.d("DetailActivity", "onCreate: $it")

            binding.tvNameDetail.text = it.name_indekos
            val name = it.name_indekos
            if (name != null) {
                label = name
            }
            val id = it.userId
            if (id != null) {
                userId = id
            }
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

            val lat = it.latitude_indekos
            if (lat != null) {
                latitude = lat
            }
            val lng = it.longitude_indekos
            if (lng != null) {
                longitude = lng
            }

            Log.d("DetailActivity", "Banner: ${it.photoBannerUrl}")
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

        binding.btnHubungiPenjual.setOnClickListener {
            if (userId != null) {
                viewModel.getUserById(userId!!).observe(this) { user ->
                    val phoneNumber = user.noTelp
                    val name = user.username
                    val message = "Halo $name, saya tertarik dengan kosan anda\nApakah masih tersedia ???."

                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}")
                    startActivity(intent)
                }
            }
            else {
                Toast.makeText(this, "Tidak ditemukan nomor telepon", Toast.LENGTH_SHORT).show()
            }
        }

        binding.clDetail.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = binding.clDetail.scrollY
            // Move the cardDetail up
            binding.cardDetail.translationY = -scrollY.toFloat() / 2
            // Shrink the bannerDetail
            val scale = 1 - scrollY.toFloat() / (bannerHeight * 2)
            binding.ivPhotoBannerDetail.scaleX = scale
            binding.ivPhotoBannerDetail.scaleY = scale
        }
    }
}