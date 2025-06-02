package com.example.kidconnect

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PengumumanActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnKirim: ImageButton
    private lateinit var etPengumuman: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pengumuman_gr)

        btnBack = findViewById(R.id.btnBack)
        btnKirim = findViewById(R.id.btnKirim)
        etPengumuman = findViewById(R.id.etPengumuman)

        btnBack.setOnClickListener {
            finish()
        }

        // Tombol kirim pengumuman
        btnKirim.setOnClickListener {
            val pengumumanText = etPengumuman.text.toString().trim()
            if (pengumumanText.isEmpty()) {
                Toast.makeText(this, "Pengumuman tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: Simpan pengumuman ke database / backend
                Toast.makeText(this, "Pengumuman terkirim:\n$pengumumanText", Toast.LENGTH_LONG).show()
                // Misal: clear input setelah kirim
                etPengumuman.setText("")
            }
        }
    }
}
