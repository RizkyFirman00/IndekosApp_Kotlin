package com.example.indekos.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.indekos.R
import com.example.indekos.databinding.ActivityMainBinding

class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}