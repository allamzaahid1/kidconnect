package com.example.kidconnect

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var loginButton: ImageButton
    private lateinit var registerButton: ImageButton
    private lateinit var tvDaftar: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        loginButton = findViewById(R.id.login)
        registerButton = findViewById(R.id.register)
        tvDaftar = findViewById(R.id.tvDaftar)

        loginButton.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val uid = auth.currentUser?.uid
                            dbRef.child(uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val role = snapshot.child("role").getValue(String::class.java)
                                    if (role == "ortu") {
                                        startActivity(Intent(this@LoginActivity, OrtuActivity::class.java))
                                        finish()
                                    } else if (role == "guru") {
                                        startActivity(Intent(this@LoginActivity, GuruActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this@LoginActivity, "Role tidak dikenal", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@LoginActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                                }
                            })
                        } else {
                            Toast.makeText(this, "Login gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
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
