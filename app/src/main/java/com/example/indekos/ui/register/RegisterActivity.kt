package com.example.indekos.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.indekos.R
import com.example.indekos.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}