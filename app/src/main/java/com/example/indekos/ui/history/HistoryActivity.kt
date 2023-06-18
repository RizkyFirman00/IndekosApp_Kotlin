package com.example.indekos.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.indekos.R
import com.example.indekos.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}