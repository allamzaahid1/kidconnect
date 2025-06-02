package com.example.kidconnect

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FeedbackActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnKirim: ImageButton
    private lateinit var etSaran: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback_ot)

        // Inisialisasi komponen
        btnBack = findViewById(R.id.btnBack)
        btnKirim = findViewById(R.id.btnKirim)
        etSaran = findViewById(R.id.etSaran)

        // Tombol kembali
        btnBack.setOnClickListener {
            onBackPressed()
        }

        // Tombol kirim
        btnKirim.setOnClickListener {
            val saran = etSaran.text.toString().trim()
            if (saran.isEmpty()) {
                Toast.makeText(this, "Pesan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: Simpan saran ke database atau kirim ke server
                Toast.makeText(this, "Saran terkirim:\n$saran", Toast.LENGTH_LONG).show()
                etSaran.setText("") // Kosongkan setelah kirim
            }
        }
    }
}
