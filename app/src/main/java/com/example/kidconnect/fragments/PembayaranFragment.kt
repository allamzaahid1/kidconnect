package com.example.kidconnect.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kidconnect.R

class PembayaranFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pembayaran_ot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tombol back
        val backButton = view.findViewById<ImageButton>(R.id.backpembayaran)
        backButton.setOnClickListener {
            val fragmentTransaction = requireActivity()
                .supportFragmentManager
                .beginTransaction()
            fragmentTransaction.replace(R.id.container, HomeFragment())
            fragmentTransaction.commit()
        }

        // Tombol notifikasi
        val btnNotif = view.findViewById<ImageButton>(R.id.notifpembayaran)
        btnNotif.setOnClickListener {
            Toast.makeText(requireContext(), "Notifikasi dibuka", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke fragment notifikasi
        }

        // Tombol pembayaran SPP
        val btnSpp = view.findViewById<ImageButton>(R.id.pembayaranspp)
        btnSpp.setOnClickListener {
            Toast.makeText(requireContext(), "Menu Pembayaran SPP dibuka", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke fragment pembayaran spp
        }

        // Tombol pembayaran Gedung
        val btnGedung = view.findViewById<ImageButton>(R.id.pembayarangedung)
        btnGedung.setOnClickListener {
            Toast.makeText(requireContext(), "Menu Pembayaran Gedung dibuka", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke fragment pembayaran gedung
        }

        // Tombol riwayat (ImageView, bukan ImageButton!)
        val btnRiwayat = view.findViewById<ImageView>(R.id.riwayat)
        btnRiwayat.setOnClickListener {
            Toast.makeText(requireContext(), "Menu Riwayat dibuka", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke fragment riwayat pembayaran
        }
    }
}
