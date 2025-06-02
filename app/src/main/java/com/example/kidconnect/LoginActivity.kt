package com.example.kidconnect

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var loginButton: ImageButton
    private lateinit var registerButton: ImageButton
    private lateinit var tvDaftar: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        loginButton = findViewById(R.id.login)
        registerButton = findViewById(R.id.register)
        tvDaftar = findViewById(R.id.tvDaftar)

        loginButton.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                // Contoh logika hak akses (sementara)
                if (username == "ortu" && password == "1234") {
                    Toast.makeText(this, "Login sebagai Orang Tua", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, OrtuActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (username == "guru" && password == "4321") {
                    Toast.makeText(this, "Login sebagai Guru", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, GuruActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        tvDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
