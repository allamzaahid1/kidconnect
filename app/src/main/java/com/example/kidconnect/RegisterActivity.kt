package com.example.kidconnect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var backButton: ImageButton
    private lateinit var etUsername: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfPassword: EditText
    private lateinit var registerBtn: ImageButton
    private lateinit var tvLogin: TextView

    private lateinit var etNamaAnak: EditText
    private lateinit var etNIP: EditText

    private var role: String = ""  // "ortu" atau "guru"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register)

        auth = FirebaseAuth.getInstance()

        backButton = findViewById(R.id.back)
        backButton.setOnClickListener {
            // Kembali ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        etUsername = findViewById(R.id.etRegUsername)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etRegPassword)
        etConfPassword = findViewById(R.id.etConfPassword)
        registerBtn = findViewById(R.id.register)
        tvLogin = findViewById(R.id.tvLogin)

        etNamaAnak = findViewById(R.id.etNamaAnak)
        etNIP = findViewById(R.id.etNIP)

        val ortuBtn = findViewById<ImageButton>(R.id.ortu)
        val guruBtn = findViewById<ImageButton>(R.id.guru)

        ortuBtn.setOnClickListener {
            role = "ortu"
            ortuBtn.setBackgroundResource(R.drawable.button_transparent)
            guruBtn.setBackgroundResource(R.drawable.button_no_border)
            etNamaAnak.visibility = View.VISIBLE
            etNIP.visibility = View.GONE
        }

        guruBtn.setOnClickListener {
            role = "guru"
            ortuBtn.setBackgroundResource(R.drawable.button_no_border)
            guruBtn.setBackgroundResource(R.drawable.button_transparent)
            etNamaAnak.visibility = View.GONE
            etNIP.visibility = View.VISIBLE
        }

        registerBtn.setOnClickListener {
            registerUser()
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val username = etUsername.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val confPassword = etConfPassword.text.toString()

        val namaAnak = etNamaAnak.text.toString().trim()
        val nip = etNIP.text.toString().trim()

        if (username.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()
            || confPassword.isEmpty() || role.isEmpty() ||
            (role == "ortu" && namaAnak.isEmpty()) ||
            (role == "guru" && nip.isEmpty())
        ) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confPassword) {
            Toast.makeText(this, "Kata sandi tidak cocok", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    val dbRef = FirebaseDatabase.getInstance().getReference("Users").child(uid!!)

                    val userData = mutableMapOf<String, String>(
                        "username" to username,
                        "phone" to phone,
                        "email" to email,
                        "role" to role
                    )

                    if (role == "ortu") userData["namaAnak"] = namaAnak
                    if (role == "guru") userData["nip"] = nip

                    dbRef.setValue(userData).addOnCompleteListener {
                        Toast.makeText(this, "Pendaftaran berhasil sebagai $role", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Gagal mendaftar: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

}
