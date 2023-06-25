package com.example.indekos.ui.history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.indekos.R
import com.example.indekos.databinding.ActivityHistoryBinding
import com.example.indekos.ui.addData.AddDataActivity

class HistoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBackToAddData.setOnClickListener {
            Intent(this, AddDataActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}