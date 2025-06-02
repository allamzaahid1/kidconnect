package com.example.kidconnect

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private lateinit var btnBack: ImageButton
private lateinit var btnNotif: ImageButton

class DetailLaporanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_laporan_ot)

        btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnNotif = findViewById<ImageButton>(R.id.notiflaporan)

        val judul = intent.getStringExtra("judul")
        val pengajar = intent.getStringExtra("pengajar")
        val deskripsi = intent.getStringExtra("deskripsi")
        val waktu = intent.getStringExtra("waktu")

        findViewById<TextView>(R.id.profiletxt).text = judul
        findViewById<TextView>(R.id.tvpeserta).text = pengajar
        findViewById<TextView>(R.id.tvdeskripsi).text = deskripsi
        findViewById<TextView>(R.id.tvwaktu).text = waktu

        btnBack.setOnClickListener {
            finish()
        }

        btnNotif.setOnClickListener {
            Toast.makeText(this, "Notifikasi dibuka", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke fragment notifikasi
        }
    }
}
