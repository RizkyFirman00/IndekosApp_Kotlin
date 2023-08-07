package com.example.indekos.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.indekos.databinding.ActivityLoginBinding
import com.example.indekos.ui.addData.AddDataActivity
import com.example.indekos.ui.admin.AdminActivity
import com.example.indekos.ui.register.RegisterActivity
import com.example.indekos.ui.splash.SplashScreenActivity
import com.example.indekos.util.Preferences
import com.example.indekos.util.ViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onBackPressed() {
        val intent = Intent(this, SplashScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Preferences.checkUsername(this)) {
            Intent(this, AddDataActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        binding.btnToregister.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username == "admin" || password == "admin00000") {
                Toast.makeText(
                    this@LoginActivity, "Login berhasil sebagai admin", Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@LoginActivity, AdminActivity::class.java).apply {
                    putExtra("userId", "admin")
                }
                startActivity(intent)
                finish()
            } else {
                lifecycleScope.launch {
                    if (viewModel.checkCredentials(username, password)) {
                        val userId = viewModel.getUserId(username)
                            ?.let { user -> Preferences.saveUserId(user, this@LoginActivity) }
                        Log.d(
                            "LoginActivity",
                            "Login Activity: UserId ${Preferences.getUserId(this@LoginActivity)}"
                        )

                        Preferences.saveUsername(username, this@LoginActivity)
                        val intent = Intent(this@LoginActivity, AddDataActivity::class.java).apply {
                            putExtra("userId", userId.toString())
                        }
                        Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity, "Username atau password salah", Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }
    }
}