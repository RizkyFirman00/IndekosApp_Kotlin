package com.example.indekos.ui.history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.indekos.databinding.ActivityHistoryBinding
import com.example.indekos.ui.detailHistory.DetailHistoryActivity
import com.example.indekos.ui.addData.AddDataActivity
import com.example.indekos.util.Preferences
import com.example.indekos.util.ViewModelFactory
import com.example.indekos.util.adapter.IndekosHistoryAdapter

class HistoryActivity : AppCompatActivity() {
    private val userId by lazy { Preferences.getUserId(this@HistoryActivity) }
    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }
    private lateinit var adapter: IndekosHistoryAdapter
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        adapter = IndekosHistoryAdapter(
            onItemClick = {
                navigateToDetailHistoryActivity(it)
            }
        )
        binding.rvDataHistory.adapter = adapter
        binding.rvDataHistory.layoutManager = LinearLayoutManager(this)

        userId?.let { viewModel.getByUserId(it.toInt()) }
        viewModel.indekosList.observe(this) { indekosList ->
            if (indekosList.isNullOrEmpty()) {
                binding.tvNoData.visibility = android.view.View.VISIBLE
            } else {
                binding.tvNoData.visibility = android.view.View.GONE
                adapter.submitList(indekosList)
            }
        }

        binding.btnBackToAddData.setOnClickListener {
            Intent(this, AddDataActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    private fun navigateToDetailHistoryActivity(indekosId: String) {
        val intent = Intent(this, DetailHistoryActivity::class.java)
        intent.putExtra("indekosId", indekosId)
        startActivity(intent)
    }
}