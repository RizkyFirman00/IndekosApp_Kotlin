package com.example.indekos.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.indekos.R
import com.example.indekos.databinding.ActivityDetailBinding
import com.example.indekos.ui.home.HomeActivity

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val recyclerView by lazy { binding.rvPhotosDetail }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager

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