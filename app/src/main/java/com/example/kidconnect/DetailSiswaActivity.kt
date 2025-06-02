package com.example.kidconnect

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailSiswaActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnNotif: ImageButton
    private lateinit var linearDaftarSiswa: LinearLayout
    private lateinit var kelastxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_siswa_gr)

        btnBack = findViewById(R.id.btnBack)
        btnNotif = findViewById(R.id.notifsiswa)
        linearDaftarSiswa = findViewById(R.id.linearDaftarSiswa)
        kelastxt = findViewById(R.id.kelastxt)

        btnBack.setOnClickListener {
            finish()
        }

        btnNotif.setOnClickListener {
            Toast.makeText(this, "Notifikasi dibuka", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke fragment notifikasi
        }

        // Ambil kelas dari Intent
        val kelas = intent.getStringExtra("kelas") ?: "A"

        // Set teks kelas sesuai data Intent
        kelastxt.text = "KELAS $kelas"

        // Pilih daftar siswa berdasarkan kelas
        val daftarSiswa = when (kelas) {
            "A" -> listOf("Arhan Purnama", "Nayla Purnama", "Andi Saputra")
            "B" -> listOf("Budi Santoso", "Sari Wulandari", "Dewi Lestari")
            else -> listOf()
        }

        // Kosongkan dulu daftar siswa sebelumnya
        linearDaftarSiswa.removeAllViews()

        val inflater = LayoutInflater.from(this)

        daftarSiswa.forEach { nama ->
            val viewItem = inflater.inflate(R.layout.item_siswa, linearDaftarSiswa, false)

            val namaSiswa = viewItem.findViewById<TextView>(R.id.namaSiswa)
            val iconSiswa = viewItem.findViewById<ImageView>(R.id.iconSiswa)

            namaSiswa.text = nama

            // Contoh: ganti icon jika nama mengandung "Nayla" atau "Sari" (bisa disesuaikan)
            if (nama.contains("Nayla", ignoreCase = true) || nama.contains("Sari", ignoreCase = true)) {
                iconSiswa.setImageResource(R.drawable.siswa_ce)
            } else {
                iconSiswa.setImageResource(R.drawable.siswa_co)
            }

            linearDaftarSiswa.addView(viewItem)
        }
    }
}
