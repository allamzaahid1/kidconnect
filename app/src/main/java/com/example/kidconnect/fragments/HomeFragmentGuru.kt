package com.example.kidconnect.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kidconnect.InputLaporanActivity
import com.example.kidconnect.LoginActivity
import com.example.kidconnect.PengumumanActivity
import com.example.kidconnect.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class HomeFragmentGuru : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var tvName: TextView
    private lateinit var tvAnnouncement: TextView
    private lateinit var tambahPengumuman: ImageButton
    private lateinit var tambahAktivitas: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_gr, container, false)

        val btnBack = view.findViewById<ImageButton>(R.id.backhomegr)
        btnBack.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val btnNotif = view.findViewById<ImageButton>(R.id.btnNotif)
        btnNotif.setOnClickListener {
            Toast.makeText(requireContext(), "Notifikasi dibuka", Toast.LENGTH_SHORT).show()
        }

        tvName = view.findViewById(R.id.tvName)
        tvAnnouncement = view.findViewById(R.id.tvAnnouncement)
        tambahPengumuman = view.findViewById(R.id.tambahpengumuman)
        tambahAktivitas = view.findViewById(R.id.tambahaktivitas)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid

            // Ambil nama guru
            database.child("Users").child(uid).get().addOnSuccessListener { userSnapshot ->
                val name = userSnapshot.child("username").value?.toString() ?: "Guru"
                tvName.text = name
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal mengambil nama guru", Toast.LENGTH_SHORT).show()
            }

            // Cari kelas yang diajar guru
            database.child("Kelas").get().addOnSuccessListener { kelasSnapshot ->
                var kelasGuru: String? = null
                for (kelas in kelasSnapshot.children) {
                    val guruId = kelas.child("guruId").value?.toString()
                    if (guruId == uid) {
                        kelasGuru = kelas.key
                        break
                    }
                }

                // Ambil pengumuman berdasarkan kelas
                if (kelasGuru != null) {
                    database.child("PengumumanPerKelas").child(kelasGuru).get()
                        .addOnSuccessListener { pengumumanSnapshot ->
                            val isi = pengumumanSnapshot.child("isi").value?.toString()
                                ?: "Belum ada pengumuman"
                            tvAnnouncement.text = isi
                        }
                        .addOnFailureListener {
                            tvAnnouncement.text = "Gagal memuat pengumuman"
                        }
                } else {
                    tvAnnouncement.text = "Guru belum mengajar kelas manapun"
                }
            }.addOnFailureListener {
                tvAnnouncement.text = "Gagal memuat data kelas"
            }
        }

        tambahPengumuman.setOnClickListener {
            val intent = Intent(requireContext(), PengumumanActivity::class.java)
            startActivity(intent)
        }

        tambahAktivitas.setOnClickListener {
            val intent = Intent(requireContext(), InputLaporanActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}
