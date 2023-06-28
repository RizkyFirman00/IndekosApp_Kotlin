package com.example.indekos.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.indekos.R
import com.example.indekos.databinding.ActivitySplashScreenBinding
import com.example.indekos.ui.home.HomeActivity
import com.example.indekos.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnGettingStarted.setOnClickListener {
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnLogin.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}