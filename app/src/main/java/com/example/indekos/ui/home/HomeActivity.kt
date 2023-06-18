package com.example.indekos.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.indekos.R
import com.example.indekos.databinding.ActivityMainBinding
import com.example.indekos.ui.detail.DetailActivity
import com.example.indekos.ui.login.LoginActivity

class HomeActivity : AppCompatActivity() {
    override fun onBackPressed() {
        finishAffinity()
    }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tvLocation.setOnClickListener {
            startActivity(Intent(this, DetailActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}