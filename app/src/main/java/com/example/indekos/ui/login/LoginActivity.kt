package com.example.indekos.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.indekos.R
import com.example.indekos.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}