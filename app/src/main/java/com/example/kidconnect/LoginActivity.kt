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
        setContentView(R.layout.layout_login) // Ganti dengan nama layout-mu jika berbeda

        // Inisialisasi view
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        loginButton = findViewById(R.id.login)
        registerButton = findViewById(R.id.register)
        tvDaftar = findViewById(R.id.tvDaftar)

        // Aksi ketika tombol login ditekan
        loginButton.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: Ganti logika ini dengan autentikasi backend
                if (username == "ortu" && password == "1234") {
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                    // Pindah ke halaman utama
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Aksi ketika tombol daftar ditekan
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Alternatif tombol teks "Daftar"
        tvDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
