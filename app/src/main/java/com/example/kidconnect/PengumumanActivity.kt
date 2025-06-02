package com.example.kidconnect

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class PengumumanActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    private lateinit var btnBack: ImageButton
    private lateinit var btnKirim: ImageButton
    private lateinit var etPengumuman: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pengumuman_gr)

        database = FirebaseDatabase.getInstance().reference

        btnBack = findViewById(R.id.btnBack)
        btnKirim = findViewById(R.id.btnKirim)
        etPengumuman = findViewById(R.id.etPengumuman)

        btnBack.setOnClickListener {
            finish()
        }

        btnKirim.setOnClickListener {
            val pengumumanText = etPengumuman.text.toString().trim()
            if (pengumumanText.isEmpty()) {
                Toast.makeText(this, "Pengumuman tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                val uidGuru = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener

                // Cari kelas yang diajar guru
                database.child("Kelas").get().addOnSuccessListener { kelasSnapshot ->
                    var kelasGuru: String? = null
                    for (kelas in kelasSnapshot.children) {
                        val guruId = kelas.child("guruId").value?.toString()
                        if (guruId == uidGuru) {
                            kelasGuru = kelas.key
                            break
                        }
                    }

                    if (kelasGuru != null) {
                        val dataPengumuman = mapOf(
                            "isi" to pengumumanText,
                            "waktu" to System.currentTimeMillis(),
                            "pengirim" to "Guru"
                        )

                        database.child("PengumumanPerKelas").child(kelasGuru).setValue(dataPengumuman)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Pengumuman dikirim ke kelas $kelasGuru", Toast.LENGTH_SHORT).show()
                                etPengumuman.setText("")
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Gagal mengirim: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Tidak ditemukan kelas untuk guru ini", Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener {
                    Toast.makeText(this, "Gagal mendapatkan data kelas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
