package com.example.indekos.ui.addData

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.indekos.databinding.ActivityAddDataBinding
import com.example.indekos.ui.history.HistoryActivity
import com.example.indekos.ui.home.HomeActivity

class AddDataActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddDataBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnToExplore.setOnClickListener {
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        binding.btnToAllData.setOnClickListener {
            Intent(this, HistoryActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}