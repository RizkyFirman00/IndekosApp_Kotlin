package com.example.indekos.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.indekos.R
import com.example.indekos.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}