package com.example.indekos.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.indekos.databinding.ActivityMainBinding
import com.example.indekos.ui.detail.DetailActivity
import com.example.indekos.ui.login.LoginActivity
import com.example.indekos.ui.splash.SplashScreenActivity
import com.example.indekos.util.ViewModelFactory
import com.example.indekos.util.adapter.IndekosHomeAdapter

class HomeActivity : AppCompatActivity() {
    override fun onBackPressed() {
        val intent = Intent(this, SplashScreenActivity::class.java)
        startActivity(intent)
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        val adapter = IndekosHomeAdapter{
            navigateToDetailActivity(it)
            Log.d("HomeActivity", "IndekosId: $it")
        }
        binding.rvKost.adapter = adapter
        binding.rvKost.layoutManager = LinearLayoutManager(this)

        viewModel.getAllIndekos()
        viewModel.indekosList.observe(this) {
            Log.d("HomeActivity", "onCreate: $it")
            adapter.submitList(it)
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun navigateToDetailActivity(indekosId: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("indekosId", indekosId)
        Log.d("HomeActivity navigateToDetailActivity", "indekosId: $indekosId")
        startActivity(intent)
    }
}