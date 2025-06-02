package com.example.kidconnect.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kidconnect.PengumumanActivity
import com.example.kidconnect.R

class HomeFragmentGuru : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvAnnouncement: TextView
    private lateinit var tambahPengumuman: ImageButton
    private lateinit var tambahAktivitas: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_gr, container, false)

        // Inisialisasi komponen UI
        tvName = view.findViewById(R.id.tvName)
        tvAnnouncement = view.findViewById(R.id.tvAnnouncement)
        tambahPengumuman = view.findViewById(R.id.tambahpengumuman)
        tambahAktivitas = view.findViewById(R.id.tambahaktivitas)

        // Dummy data
        val guruName = "Bu Rina"
        val pengumuman = "Membawa alat gambar untuk kegiatan besok."

        // Tampilkan data
        tvName.text = guruName
        tvAnnouncement.text = pengumuman

        // Tombol tambah pengumuman
        tambahPengumuman.setOnClickListener {
            val intent = Intent(requireContext(), PengumumanActivity::class.java)
            startActivity(intent)
        }

        // Tombol tambah aktivitas
        tambahAktivitas.setOnClickListener {
            Toast.makeText(requireContext(), "Tambah Aktivitas diklik", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke tambah aktivitas
        }

        return view
    }
}
