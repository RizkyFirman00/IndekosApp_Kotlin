package com.example.indekos.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.indekos.databinding.ActivityAdminBinding
import com.example.indekos.ui.detailHistory.DetailHistoryActivity
import com.example.indekos.ui.login.LoginActivity
import com.example.indekos.util.ViewModelFactory
import com.example.indekos.util.adapter.IndekosAdminAdapter

class AdminActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAdminBinding.inflate(layoutInflater) }
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var adapter: IndekosAdminAdapter
    private val viewModel by viewModels<AdminViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, "Logout Berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setupItemTouchHelper()

        adapter = IndekosAdminAdapter(
            onItemClick = {
                navigateToDetailHistoryActivity(it)
            }
        )
        binding.rvAdmin.adapter = adapter
        binding.rvAdmin.layoutManager = LinearLayoutManager(this)

        binding.svAdmin.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchIndekos(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchIndekos(newText)
                return true
            }
        })

        viewModel.getAllIndekos()
        viewModel.indekosList.observe(this) { indekosList ->
            if (indekosList.isNullOrEmpty()) {
                binding.progressBar2.visibility = android.view.View.GONE
                binding.tvDataKosong.visibility = android.view.View.VISIBLE

            } else {
                binding.progressBar2.visibility = android.view.View.GONE
                adapter.submitList(indekosList)
            }
        }

    }

    private fun navigateToDetailHistoryActivity(indekosId: String) {
        val intent = Intent(this, DetailHistoryActivity::class.java)
        intent.putExtra("indekosId", indekosId)
        startActivity(intent)
    }

    private fun setupItemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT // Set the swipe directions (in this case, right)
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteIndekos(position)
            }
        }

        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvAdmin)
    }
}